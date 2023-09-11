package com.github.klefstad_teaching.cs122b.movies.model.request;

public class MovieSearchByPersonRequest {
    private Integer limit;
    private Integer page;
    private String orderBy;
    private String direction;

    public Integer getLimit() {
        return limit;
    }

    public MovieSearchByPersonRequest setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public Integer getPage() {
        return page;
    }

    public MovieSearchByPersonRequest setPage(Integer page) {
        this.page = page;
        return this;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public MovieSearchByPersonRequest setOrderBy(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    public String getDirection() {
        return direction;
    }

    public MovieSearchByPersonRequest setDirection(String direction) {
        this.direction = direction;
        return this;
    }
}
