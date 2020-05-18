package io.hint.annotation;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.stream.Collectors;

public class NativeImageAnnotationProcessor extends AbstractProcessor {
    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // Iterate over all @Hint annotated elements
        List<TypeElement> hintClasses = new ArrayList<>();
        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(Hint.class)) {
            // Check if a class has been annotated with @Hint
            if (annotatedElement.getKind() != ElementKind.CLASS) {
                messager.printMessage(Diagnostic.Kind.ERROR,
                        String.format("Only classes can be annotated with @%s", Hint.class.getSimpleName()),
                        annotatedElement);
                return true; // Exit processing
            }

            // We can cast it, because we know that it of ElementKind.CLASS
            TypeElement typeElement = (TypeElement) annotatedElement;
            hintClasses.add(typeElement);

            messager.printMessage(Diagnostic.Kind.NOTE,
                    String.format("Adding class %s with annotation @%s to reflection configuration",
                            Hint.class.getSimpleName(), typeElement.getSimpleName()), annotatedElement);
        }

        Map<TypeElement, List<Element>> hintMessageElements = new HashMap<>();

        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(HintMessage.class)) {
            // Check if a class/method has been annotated with @HintMessage
            if (annotatedElement.getKind() == ElementKind.CLASS) {
                TypeElement typeElement = (TypeElement) annotatedElement;
                if (!hintMessageElements.containsKey(typeElement)) {
                    hintMessageElements.put(typeElement, new ArrayList<>());
                    messager.printMessage(Diagnostic.Kind.NOTE,
                            String.format("Adding class %s with annotation @%s to reflection configuration",
                                    HintMessage.class.getSimpleName(), typeElement.getSimpleName()),
                            annotatedElement);
                }
            } else if (annotatedElement.getKind() == ElementKind.METHOD) {
                if (annotatedElement.getEnclosingElement().getKind() == ElementKind.CLASS) {
                    TypeElement classElement = (TypeElement) annotatedElement.getEnclosingElement();
                    if (hintMessageElements.containsKey(classElement)) {
                        List<Element> methods = hintMessageElements.get(classElement);
                        methods.add(annotatedElement);
                        hintMessageElements.put(classElement, methods);
                    } else {
                        List<Element> elements = new ArrayList<>();
                        elements.add(annotatedElement);
                        hintMessageElements.put(classElement, elements);
                    }
                    messager.printMessage(Diagnostic.Kind.NOTE,
                            String.format(
                                    "Adding method %s with annotation @%s in class %s to reflection configuration",
                                    annotatedElement,
                                    HintMessage.class.getSimpleName(),
                                    classElement.getSimpleName()), annotatedElement);
                }
            } else {
                messager.printMessage(Diagnostic.Kind.ERROR,
                        String.format("Only classes and methods can be annotated with @%s",
                                HintMessage.class.getSimpleName()),
                        annotatedElement);
                return true; // Exit processing
            }
        }

        if (!roundEnv.processingOver()) {
            // generate config file
            String resourceFile = "META-INF/native-image/reflect-config.json";
            try {
                FileObject newFile = filer.createResource(StandardLocation.CLASS_OUTPUT,
                        "", resourceFile);
                Writer writer = newFile.openWriter();
                BufferedWriter bufferedWriter = new BufferedWriter(writer);

                bufferedWriter.write("[\n");

                String classReflectTemplate =
                        "\t{\n" +
                                "\t\t\"name\":\"%s\"\n" +
                                "\t}";

                String fileContent = hintClasses
                        .stream()
                        .map(c -> String.format(classReflectTemplate, c.getQualifiedName()))
                        .collect(Collectors.joining(",\n"));

                bufferedWriter.write(fileContent);

                String hintMsgClassReflectTemplate =
                        "\t{\n" +
                                "\t\t\"name\" : \"%s\",\n" +
                                "\t\t\t\"methods\" : [\n" +
                                "%s\n" +
                                "\t\t\t]\n" +
                                "\t}";

                String hintMsgMethodReflectTemplate = "\t\t\t\t{ \"name\" : \"%s\" }";

                String finalHintMsgContent = hintMessageElements.entrySet().stream().map(e -> {
                    // e.getKey() => enclosingElement
                    // e.getValue() => list of methods
                    String methodsForReflect = e.getValue()
                            .stream()
                            .map(m -> String.format(hintMsgMethodReflectTemplate, m.getSimpleName()))
                            .collect(Collectors.joining(",\n"));

                    return String.format(hintMsgClassReflectTemplate, e.getKey().getQualifiedName(), methodsForReflect);
                }).collect(Collectors.joining(",\n"));

                if (!fileContent.isEmpty() && !finalHintMsgContent.isEmpty()) {
                    bufferedWriter.write(",\n" + finalHintMsgContent);
                }

                bufferedWriter.write("\n]");

                bufferedWriter.close();
                writer.close();
            } catch (IOException e) {
                messager.printMessage(Diagnostic.Kind.ERROR,
                        String.format("Can't generate reflection configuration due to : @%s", e.getMessage()));
            }
        }
        return true;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(Hint.class.getCanonicalName());
        annotations.add(HintMessage.class.getCanonicalName());
        return annotations;
    }
}
