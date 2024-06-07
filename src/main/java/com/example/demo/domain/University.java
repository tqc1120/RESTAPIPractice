package com.example.demo.domain;

import java.util.List;

public class University {
    private String name;
    private String domain;
    private List<String> web_pages;

    public University() {
    }

    public University(String name, String domain, List<String> web_pages) {
        this.name = name;
        this.domain = domain;
        this.web_pages = web_pages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public List<String> getWeb_pages() {
        return web_pages;
    }

    public void setWeb_pages(List<String> web_pages) {
        this.web_pages = web_pages;
    }
}
