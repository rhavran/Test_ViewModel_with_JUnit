package com.dummy.myapplication.data.api;

import java.util.Objects;

public class ManagerNameAndEmail {
    private String email;
    private String name;

    ManagerNameAndEmail(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ManagerNameAndEmail that = (ManagerNameAndEmail) o;
        return Objects.equals(email, that.email) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, name);
    }
}
