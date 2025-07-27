package com.alura.literalura.dtos;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GutendexApiResponse(List<GutendexApiResponse.Book> results)
{
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Book (
            String title,
            List<Author> authors,
            List<String> languages,
            @JsonProperty("download_count") Long downloadCount

            )
    {
        public String getPrimaryLanguage() {
            return languages != null && !languages.isEmpty()
                    ? languages.getFirst()
                    : null;
        }
    };

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Author(
            String name,
            @JsonProperty("birth_year") Integer birthYear,
            @JsonProperty("death_year") Integer deathYear
    )
    {
        public String getLifeYears() {
            return birthYear + "-" + (deathYear != null ? deathYear : "Present");
        }
    }
}
