Validez
========

`Validez` is a Java Library for generating validators for DTO objects using annotation processing.

Remove boilerplate code of writing validation layers for objects, just put several annotations on fields
that you need to validate in object, and use generated validators.

### Basic usage
Here's some DTO object with personal information

```java
package example;

public class PersonalInformation {
    
    private String name;
    private String surname;
    private String phoneNumber;
    private int age;
    
    //getters/setters etc.
}

```
Let's say we want to impose restrictions on the allowed values in the fields of this object:

- name length must be greater than 3 symbols and less than 128 symbols
- surname length must be greater than 3 symbols and less than 64 symbols
- phone number length must be exactly 11 symbols
- age must be more than 18

In classic approach to validate this object for our business layer code, we need to write something like this:

```java
public boolean validate(PersonalInformation personalInformation) {
    String name = personalInformation.getName();
    if (name.lenght() > 128 || name.length() < 3) {
        return false;
    }
    String surname = personalInformation.getSurname();
    if (surname.lenght() > 64 || surname.length() < 3) {
       return false;
    }
    String phoneNumber = personalInformation.getPhoneNumber();
    if (phoneNumber.lenght() != 11) {
        return false;
    }
    int age = personalInformation.getAge();
    if (age < 18) {
        return false;
    }
    return true;
}
```

Using `validez` it can be done much easier. Let's add some annotations to PersonalInformation class:

```java
package example;

import validez.lib.annotation.validators.Length;
import validez.lib.annotation.validators.IntBound;
import validez.lib.annotation.Validate;

@Validate
public class PersonalInformation {

    @Length(max = 128, min = 3)
    private String name;
    @Length(max = 64, min = 3)
    private String surname;
    @Length(equals = 11)
    private String phoneNumber;
    @IntBound(min = 18)
    private int age;

    //getters/setters etc.
}

```
Now our class marked with `@Validate` - it's says that this class needs to generate validator for it.
Generated validator will use fields, which marked with relevant validators annotations (`@Length`, `@IntBound`)
for validate instances of PersonalInformation.

To use validator we need to simply allocate it and call `validate` method on it:

```java
import example.PersonalInformationValidatorImpl; //1
import validez.lib.api.Validator;
import validez.lib.api.data.ValidationResult;

public boolean validatePersonalInformation(PersonalInformation personalInformation) {
    Validator<PersonalInformation> validator = new PersonalInformationValidatorImpl(); //2
    ValidationResult result = validator.validate(personalInformation);
    return result.isValid(); //3
}
```

Let's describe marked parts of code:

1. Validator will be generated in the same package as DTO class and its name will be same with ValidatorImpl postfix.
2. Generated validator implements `validez.lib.api.Validator` interface with 2 type parameters: DTO type and throwing exception type
3. Validator returns special object of type `validez.lib.api.data.ValidationResult` which encapsulate validation result

Also, this code can be simplified using `validez.lib.api.Validators` utility class:

```java
import validez.lib.api.Validators;
import validez.lib.api.data.ValidationResult;

public boolean validatePersonalInformation(PersonalInformation personalInformation) {
    ValidationResult result = Validators.validate(personalInformation);
    return result.isValid();
}
```
In internals, `Validators` will extract validator for target object and call `validate` method.

All available validators can be founded in `validez.lib.annotation.validators` package
with all required documentation.

### Validation result object

As mentioned above `validez.lib.api.data.ValidationResult` is a special object for encapsulate result of validation.
This object has several fields:

- valid - flag, which indicates is validation proceed or not
- validatorContext - object, which store information about invalid field
- invariantContext - map of validators context, for represent all invalid invariant fields (invariant concept is described below in doc)

`validatorContext` field has type `validez.lib.api.data.ValidatorContext`, 
this object used for storing information about invalid field, what can be useful for logging/messaging purposes, 
and contains following information:

* Field name
* Validator annotation class which failed
* Validator annotation property which failed
* Actual field value
* `ValidationResult` for invalid sub-object

Everything from this list can have a null value, depending on different situations, see javadoc for more information.

For example let's validate previously created `PersonalInformation` 
object and process validation result:

```java

public void validate(PersonalInformation personalInformation) {
    ValidationResult result = Validators.validate(personalInformation);
    if (result.isValid()) return;
    ValidatorContext context = result.getValidatorContext();
    String invalidField = context.getFieldName();
    if ("name".equals(invalidField)) {
        String nameValue = context.getFieldValue();
        if (nameValue == null) {
            throw new RuntimeException("name must be non-null");
        }
        String failedProperty = context.getProperty();
        if ("max".equals(failedProperty)) {
            throw new RuntimeException("name cannot be > 128");
        } else if ("min".equals(failedProperty)) {
            throw new RuntimeException("name len must be greater than 3");
        }
    }
    //...next field checks
}

```

Or simple:

```java

public void validate(PersonalInformation personalInformation) {
    ValidationResult result = Validators.validate(personalInformation);
    if (result.isValid()) return;
    ValidatorContext context = result.getValidatorContext();
    String invalidField = context.getFieldName();
    throw new RuntimeException("%s is invalid, check API docs for help.".formatted(invalidField));
}

```

Using `ValidationResult` you may create maximum custom messaging/logging handlers for all needs.

### Validating composite objects

Composite objects will be validated by default, if type of this object also marked with
`@Validate`.
To validate composite object, validator will try to find validator for it type
and invoke validate method for it.

For example, lets add to `PersonalInformation` class field spouse of the same type:

```java
package example;

import validez.lib.annotation.Validate;

@Validate
public class PersonalInformation {
    
    //...previous fields

    private PersonalInformation spouse;
    
    //getters/setters etc.
}
```
Now, validator will validate spouse field, because it's type `PersonalInformation` marked with `@Validate`.
All other types, which are not `@Validate` will be ignored.  
To change this behaviour, if you don't want to validate such fields, you can mark them with `validez.lib.annotation.conditions.Exclude`:

```java
package example;

import validez.lib.annotation.Validate;
import validez.lib.annotation.conditions.Exclude;

@Validate
public class PersonalInformation {
    
    //...previous fields

    @Exclude
    private PersonalInformation spouse;
    
    //getters/setters etc.
}
```
Now, this field is no longer a part of validation process.

### Partial validation

In some cases needs validate not all object, only several fields. For example, our application used big DTO
for processing many http requests, and not all fields used in every request, but they marked with some validator.
For this, `validez.lib.api.Validator` interface provide two more methods `validateIncludes` and `validateExcludes`.

These methods have additional argument of type `java.util.Set<String>`, to provide field names,
which must be excluded or exclusively added to validation.  
If you want to exclude fields from validation, use `validateExcludes`, if you want to use in validation only specified fields,
use `validateIncludes`.

For example, we want to validate PersonalInformation in one process only with age, and in another, with all fields except of phoneNumber:

```java
import example.PersonalInformationValidatorImpl;
import validez.lib.api.Validator;
import java.util.Set;

public void validatePersonalInformation(PersonalInformation personalInformation) {
    Validator<PersonalInformation> validator = new PersonalInformationValidatorImpl();
    //validate only age
    validator.validateIncludes(personalInformation, Set.of("age"));
    //validate all, except of phoneNumber
    validator.validateExcludes(personalInformation, Set.of("phoneNumber"));
}
```

Also, it can be achieved using `Validators`, only difference,
is that consumes varargs for field names, for more convenience:

```java
import validez.lib.api.Validators;

public void validatePersonalInformation(PersonalInformation personalInformation) {
    //validate only age
    Validators.validateIncludes(personalInformation, "age");
    //validate all, except of phoneNumber
    Validators.validateExcludes(personalInformation, "phoneNumber");
}
```

As well, if some composite object field need to be excluded/included,
there is `validez.lib.annotation.conditions.Partial` annotation, to declare it in DTO.

```java

package example;

import validez.lib.annotation.Validate;
import validez.lib.annotation.conditions.Partial;

@Validate
public class PersonalInformation {
    
    //...previous fields

    @Partial(includes = "age")
    private PersonalInformation spouse;
    @Partial(excludes = "phoneNumber")
    private PersonalInformation exSpouse;
    
    //getters/setters etc.
}

```

For now, there is no API for dynamic exclude/include fields from composite objects. 
There is one workaround - exclude composite objects, and invoke validators for each. 

### Invariant's concept

What if we need to define some condition, there some field or fields are valid if invalid other, in the indicated order.
For example, let's create new DTO class, called `PaymentInformation`:

```java
package test;

import validez.lib.annotation.Validate;
import validez.lib.annotation.validators.Length;
import validez.lib.annotation.validators.NotNull;

import java.math.BigDecimal;

@Validate
public class PaymentInformation {

    @Length(equals = 24)
    private String recipientAccountNumber;
    @Length(equals = 11)
    private String recipientPhoneNumber;
    @Length(min = 3, max = 64)
    private String recipientName;

    @Length(min = 32)
    private String qrCode;
    @Length(equals = 128)
    private String token;

    @NotNull
    private BigDecimal amount;
    //getters, setters, etc.
}
```

There is some abstract information about money transaction which describes:
* recipient account number
* recipient phone number
* recipient name
* transaction qr code
* transaction token
* transaction amount

We want to define 'business' relations between fields and make this relation used in validation:

1. When `recipientAccountNumber` is invalid, then try to validate `recipientPhoneNumber` and `recipientName`
or fail
2. When `qrCode` is invalid, then try to validate `token`

Let's give it names:
1. paymentType: defines type of payment - by account number or by phone + name
2. paymentMethod: defines method which used for creating transaction - qr code or token

Basic usage can provide to us only validating each field separately, for doing this type of validation,
we need to define invariants:

```java
package test;

import validez.lib.annotation.Validate;
import validez.lib.annotation.validators.Length;
import validez.lib.annotation.validators.NotNull;
import validez.lib.annotation.conditions.Invariant;
import validez.lib.annotation.conditions.Fields;

import java.math.BigDecimal;

@Validate
@Invariant(name = "paymentType", members = { //1
        @Fields("recipientAccountNumber"), //2
        @Fields({"recipientPhoneNumber", "recipientName"}) //3
})
@Invariant(name = "paymentMethod", members = { //4
        @Fields("qrCode"),
        @Fields("token")
})
public class PaymentInformation {

    @Length(equals = 24)
    private String recipientAccountNumber;
    @Length(equals = 11)
    private String recipientPhoneNumber;
    @Length(min = 3, max = 64)
    private String recipientName;

    @Length(min = 32)
    private String qrCode;
    @Length(equals = 128)
    private String token;

    @NotNull
    private BigDecimal amount;
    //getters, setters, etc.
}
```
Let's describe this code:
1. Declaring invariant named paymentType, provide members of this invariant
2. List member which contains `recipientAccountNumber` field using `validez.lib.annotation.conditions.Fields` annotation
3. List member which contains fields `recipientPhoneNumber` and `recipientName`
4. Declaring invariant `paymentMethod` with members of fields `qrCode` and `token`

Each invariant will be validated using members fields, in declared order, 
between all member fields will be OR condition for validation,
remembering our task, we need:
* validate `recipientAccountNumber` if it is invalid - validate `recipientPhoneNumber` and `recipientName`
* validate `qrCode` if it is invalid - validate `token`

That's exactly what the invariant does. Describing this declaration, generated code will provide something like:

```java

public boolean validate(PaymentInformation paymentInformation) {
    String recipientAccountNumber = paymentInformation.getRecipientAccountNumber();
    boolean recipientAccountNumberValid = recipientAccountNumber != null && recipientAccountNumber.lenght() != 24;
    if (recipientAccountNumberValid) {
        String recipientPhoneNumber = paymentInformation.getRecipientPhoneNumber();
        String recipientName = paymentInformation.getRecipientName();
        boolean recipientPhoneNumberValid = recipientPhoneNumber != null && recipientPhoneNumber.length() == 11;
        boolean recipientNameValid = recipientName != null && recipientName.length() > 3 && recipientName.length() < 64;
        if (!recipientPhoneNumberValid || !recipientNameValid) {
            return false;
        }
    }
    String qrCode = paymentInformation.getQrCode();
    boolean qrCodeValid = qrCode != null && qrCode.length() > 32;
    if (!qrCodeValid) {
        String token = paymentInformation.getToken();
        boolean tokenValid = token != null && token.length() == 128;
        if (!tokenValid) {
            return false;
        }
    }
    //validate non-variant fields
    //...
    //if all valid
    return true;
}
```
### Custom validators

If no one of default validators suitable for yours case, you can create your own validator,
which will be integrated into generated code.

For example, we want to validate string fields, 
by condition of its length and specific char position value.
Let's name this validator, `CharPosition`, and imagine how it will be used in code:

```java

@CharPosition(length = 32, position = 16, value = 'F')
private String mark;
```

In our plan, this could check that string length exactly 32 char length,
and its 16 char equals to 'F'.

Now, lets create annotation:

```java
package test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
public @interface CharPosition {
    
    char value();
    int length();
    int position();
}
```

Then, we need to add functional to this annotation, for this,
we need to implement `validez.lib.api.external.ExternalValidator` and mark implementor class with
`validez.lib.annotation.external.Register` annotation, for telling processor, 
that this class used for implementing validation mechanism.

`ExternalValidator` interface provides one type parameter for annotation class. This parameter used by annotation
processor for checking if provided on field annotation implement validator, and which of them need to use.
This interface requires to implement method `validate` with parameters
`validez.lib.api.external.AnnotationProperties.AnnotationProperties` properties and `Object` property.  
`AnnotationProperties` parameter provide object which represents annotation and it's values in runtime without using reflection,
it will be filled in annotation processing time. Property parameter represent value of field, which must be validated.  
`AnnotationProperties` object has method `getValue` which returns value of annotation property. 
`AnnotationProperties` object can represent every possible annotation property value, including another annotations, which will be represented as 
`AnnotationProperties` too.  

Implementation of validator will look like this:

```java
package test;

import validez.lib.api.external.ExternalValidator;
import validez.lib.annotation.external.Register;
import validez.lib.api.external.AnnotationProperties;

@Register
public class CharPositionExternalValidator implements ExternalValidator<CharPosition> {
    public boolean validate(AnnotationProperties properties, Object property) {
        if (!(property instanceof String)) {
            return false;
        }
        String stringValue = (String) property;
        if (stringValue == null) {
            return false;
        }
        int length = stringValue.length();
        int lengthFromAnnotation = properties.getValue("length");
        if (lengthFromAnnotation != length) {
            return false;
        }
        char charValue = properties.getValue("value");
        int charPosition = properties.getValue("position");
        return charValue == stringValue.charAt(charPosition);
    }
}
```
Now, `@CharPosition` annotation can be used on fields of your classes for validating them.