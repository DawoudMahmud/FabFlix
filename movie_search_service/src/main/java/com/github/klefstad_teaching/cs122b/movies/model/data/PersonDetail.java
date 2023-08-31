package com.github.klefstad_teaching.cs122b.movies.model.data;

public class PersonDetail {
    private Integer id;
    private String name;
    private String birthday;
    private String biography;
    private String birthplace;
    private float popularity; //capital?
    private String profilePath;

    public Integer getId() {
        return id;
    }

    public PersonDetail setId(Integer id) {
        this.id = id;
        return this;
    }
    public String getBirthday() {
        return birthday;
    }

    public PersonDetail setBirthday(String birthday) {
        this.birthday = birthday;
        return this;
    }

    public String getBiography() {
        return biography;
    }

    public PersonDetail setBiography(String biography) {
        this.biography = biography.replace("\r","");
        this.biography = biography;
        return this;
    }

    public String getBirthplace() {
        return birthplace;
    }

    public PersonDetail setBirthplace(String birthplace) {
        this.birthplace = birthplace;
        return this;
    }

    public float getPopularity() {
        return popularity;
    }

    public PersonDetail setPopularity(float popularity) {
        this.popularity = popularity;
        return this;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public PersonDetail setProfilePath(String profilePath) {
        this.profilePath = profilePath;
        return this;
    }


    public String getName() {
        return name;
    }

    public PersonDetail setName(String name) {
        this.name = name;
        return this;
    }
}
