package com.github.klefstad_teaching.cs122b.movies.model.request;

public class PersonGetByPersonIdRequest {
    private Long personId;

    public Long getPersonId() {
        return personId;
    }

    public PersonGetByPersonIdRequest setPersonId(Long personId) {
        this.personId = personId;
        return this;
    }
}
