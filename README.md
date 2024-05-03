Validez
========

`Validez` is a Java Library for generating validators for DTO objects.

Remove boilerplate code of writing validation layers for objects, just put several annotations on fields
that you need to validate in object, and use generated validators with your 
custom exceptions and exceptions messages.

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
public void validate(PersonalInformation personalInformation) {
    String name = personalInformation.getName();
    if (name.lenght() > 128 || name.length() < 3) {
        throw new IllegalArgumentException("name not valid");
    }
    String surname = personalInformation.getSurname();
    if (surname.lenght() > 64 || surname.length() < 3) {
        throw new IllegalArgumentException("surname not valid");
    }
    String phoneNumber = personalInformation.getPhoneNumber();
    if (phoneNumber.lenght() != 11) {
        throw new IllegalArgumentException("phone number must be 11 symbols len");
    }
    int age = personalInformation.getAge();
    if (age < 18) {
        throw new IllegalArgumentException("age must be greater than 18");
    }
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
import validez.lib.exceptions.InvalidException;

public void validatePersonalInformation(PersonalInformation personalInformation)
        throws InvalidException { //2
    Validator<PersonalInformation, InvalidException> validator = new PersonalInformationValidatorImpl(); //3
    validator.validate(personalInformation);
}
```

Let's describe marked parts of code:

1. Validator will be generated in the same package as DTO class and its name will be same with ValidatorImpl postfix.
2. Validator throws `validez.lib.exceptions.InvalidException` by default (this can be overridden) and it is checked exception.
3. Generated validator implements `validez.lib.api.Validator` interface with 2 type parameters: DTO type and throwing exception type

Also, this code can be simplified using `validez.lib.api.Validators` utility class:

```java
import validez.lib.api.Validators;
import validez.lib.exceptions.InvalidException;

public void validatePersonalInformation(PersonalInformation personalInformation)
        throws InvalidException {
    Validators.validate(personalInformation);
}
```
In internals, `Validators` will extract validator for target object and call `validate` method.

### Overriding exceptions
By default, every generated validator will throw `validez.lib.exceptions.InvalidException`.  
In some cases may be useful to throw application or process specific exception by validator.
To override default exception you can use annotation `validez.lib.annotation.ValidatorThrows`.  
Using previous example , let's make it throw `IllegalArgumentException` exception:
```java
package example;

import validez.lib.annotation.Validate;
import validez.lib.annotation.ValidatorThrows;

@Validate
@ValidatorThrows(IllegalArgumentException.class)
public class PersonalInformation {
    //fields, getters/setters etc.
}
```
Now, generated validator method `validate` will throw `IllegalArgumentException`.

This annotation API supports any Exception, checked or unchecked, 
which has constructor with one String parameter.

Also, in some cases will be useful to make all validators throw one exception in all project. 
For remove boilerplate annotating every DTO classes with `@ValidatorThrows` you can create in root of project
file named `validez.properties`, and declare property `validator.exception`. 
Value for this property must be canonical name of the exception class.

This approaches can be used together, if declared `@ValidatorThrows` and `validez.properties`
annotation processor will use exception from `@ValidatorThrows`.

### Modifying exceptions messages

