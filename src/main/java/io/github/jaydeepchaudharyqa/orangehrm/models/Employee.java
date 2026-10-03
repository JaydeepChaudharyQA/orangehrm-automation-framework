package io.github.jaydeepchaudharyqa.orangehrm.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Objects;

/**
 * An OrangeHRM employee used as test data.
 *
 * <p>Fields are private and final and can only be set through the {@link Builder}, which
 * validates required values (encapsulation). Jackson also uses the builder when reading
 * employees from JSON.
 */
@JsonDeserialize(builder = Employee.Builder.class)
public final class Employee {

    private final String firstName;
    private final String middleName;
    private final String lastName;
    private final String employeeId;

    private Employee(Builder builder) {
        this.firstName = Objects.requireNonNull(builder.firstName, "firstName is required");
        this.middleName = builder.middleName == null ? "" : builder.middleName;
        this.lastName = Objects.requireNonNull(builder.lastName, "lastName is required");
        this.employeeId = builder.employeeId;
    }

    public static Builder builder() {
        return new Builder();
    }

    /** Returns a copy with different values, keeping this object unchanged. */
    public Builder toBuilder() {
        return new Builder().firstName(firstName).middleName(middleName).lastName(lastName).employeeId(employeeId);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    /** "First Last", the format OrangeHRM shows in the employee profile header. */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return getFullName() + (employeeId == null ? "" : " (id " + employeeId + ")");
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static final class Builder {
        private String firstName;
        private String middleName;
        private String lastName;
        private String employeeId;

        private Builder() {
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder middleName(String middleName) {
            this.middleName = middleName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder employeeId(String employeeId) {
            this.employeeId = employeeId;
            return this;
        }

        public Employee build() {
            return new Employee(this);
        }
    }
}
