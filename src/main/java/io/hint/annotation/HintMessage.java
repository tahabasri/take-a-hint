/*
   Copyright 2020 Taha BASRI

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package io.hint.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Use this annotation to set default hint message for a method or class.</p>
 *
 * <p>An example use case is when you have multiple changes to throw exceptions in same method,
 * in addition, you would like to use the same hint message for all thrown exceptions in the same method.</p>
 *
 * <p>Given the following example:</p>
 * <pre>
 *     public class Spaceship{
 *
 *        {@code @HintMessage("Check the value for i")}
 *         public void goToSpace(int i){
 *             if(i==0){
 *                 throw new RuntimeException("i is 0");
 *             }else if (i==1){
 *                 throw new RuntimeException("i = is 1");
 *             }
 *             ...
 *         }
 *     }
 * </pre>
 *
 * <p>For each exception thrown in the method {@code goToSpace},
 * the value in the annotation {@code HintMessage} will be used as default hint message.</p>
 *
 * <p>You can use this annotation at class level, each exception thrown at any method
 * of the class will use the value in the global annotation at type level.</p>
 *
 * <p><b>Note: </b>Annotation at method level overrides its parent at class type.</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface HintMessage {
    /**
     * Sets default message to show for each exception thrown at the annotated element.
     *
     * @return default exception message
     */
    String value() default "";
}
