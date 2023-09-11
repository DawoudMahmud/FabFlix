package com.github.klefstad_teaching.cs122b.movies.model.data;

public class Person {
    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public Person setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Person setName(String name) {
        this.name = name;
        return this;
    }
}
