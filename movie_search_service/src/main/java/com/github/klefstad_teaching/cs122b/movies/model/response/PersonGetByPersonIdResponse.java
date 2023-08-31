package com.github.klefstad_teaching.cs122b.movies.model.response;

import com.github.klefstad_teaching.cs122b.core.base.ResponseModel;
import com.github.klefstad_teaching.cs122b.movies.model.data.PersonDetail;

public class PersonGetByPersonIdResponse extends ResponseModel<PersonGetByPersonIdResponse> {
    private PersonDetail person;

    public PersonDetail getPerson() {
        return person;
    }

    public PersonGetByPersonIdResponse setPerson(PersonDetail person) {
        this.person = person;
        return this;
    }
}
