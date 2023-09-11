package com.github.klefstad_teaching.cs122b.movies.model.response;

import com.github.klefstad_teaching.cs122b.core.base.ResponseModel;
import com.github.klefstad_teaching.cs122b.movies.model.data.PersonDetail;

import java.util.List;

public class PersonSearchResponse extends ResponseModel<PersonSearchResponse> {
    private List<PersonDetail> persons;

    public List<PersonDetail> getPersons() {
        return persons;
    }

    public PersonSearchResponse setPersons(List<PersonDetail> persons) {
        this.persons = persons;
        return this;
    }
}
