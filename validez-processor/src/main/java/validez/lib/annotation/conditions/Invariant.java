package validez.lib.annotation.conditions;


import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

/**
 * This annotation imposes restrictions on internal state of object between fields
 * provided in {@link Invariant#members()} fields.
 * Use this annotation to declare possible combinations of field's that represent some logic together
 * Example:
 * <pre>{@code
 * @Invariant(name = "phone", members = {
 *      @Fields("mobilePhoneNumber"),
 *      @Fields("homePhoneNumber")
 * })
 * public class PhoneCatalogMember {
 *
 *      @NotEmpty
 *      private String mobilePhoneNumber
 *      @NotEmpty
 *      private String homePhoneNumber;
 *      private String name;
 *
 * }
 * }</pre>
 * In this example class contains fields mobilePhoneNumber and homePhoneNumber.
 * We want the following condition to be met when validating an object:
 * either a home phone number or a mobile phone number is valid.
 * It doesn't matter to us which one, the main limitation is that both of them cannot be absent.
 * And our object will contain at least one of the two phone numbers.
 * <br>
 * For this we impose invariant named "phone" on class PhoneCatalogMember and provide fields,
 * who are involved "mobilePhoneNumber" and "homePhoneNumber".
 * <br>
 * Between all @Fields that part of {@link Invariant#members()} the "or" relationship is established.
 * So, generated code will provide condition
 * <pre>{@code
 *  if (mobilePhoneNumber == null) {
 *      if (homePhoneNumber == null) {
 *          //object not valid
 *      }
 *      //object valid
 *  }
 *  //object valid
 * }</pre>
 * <br>
 *
 * This is repeatable annotation,
 * so it can be provided on class to create many invariants for various fields.
 */
@Target(ElementType.TYPE)
@Repeatable(Invariants.class)
public @interface Invariant {

    /**
     * Invariant name. Used to mark in source code, that invariant fields together
     * is a part of some business logic. Also generated code will include comment near invariant check.
     * @return name of invariant
     */
    String name();

    /**
     * Use this to specify fields for this invariant.
     * If some field will be listed twice or more annotation processor will throw Runtime exception
     * and compilation process will fail.
     * <br>
     * Also, exception will be thrown if some listed field not present in class
     * <br>
     * Examples:
     * <pre>{@code
     *   @Invariant(name = "myCoolInvariant", members = {
     *      @Fields(value = {"coolField1", "coolField1"}), //caused exception because coolField1 is listed twice
     *      @Fields("coolField2")
     * })
     * class MyCoolClass {
     *     private int coolField1;
     *     private int coolField2;
     * }
     *}</pre>
     * <pre>{@code
     *   @Invariant(name = "myCoolInvariant", members = {
     *      @Fields("coolField1"),
     *      @Fields("coolField1") //caused exception because coolField1 is listed twice
     * })
     * class MyCoolClass {
     *     private int coolField1;
     *     private int coolField2;
     * }
     *}</pre>
     * <pre>{@code
     *   @Invariant(name = "myCoolInvariant", members = {
     *      @Fields("coolField1"),
     *      @Fields("notCoolField") //caused exception because notCoolField is not presented in class
     * })
     * class MyCoolClass {
     *     private int coolField1;
     *     private int coolField2;
     * }
     *}</pre>
     * @see Fields
     * @return Array of fields that is a part of this invariant
     */
    Fields[] members();

}
