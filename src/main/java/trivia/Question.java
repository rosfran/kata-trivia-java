package trivia;

import lombok.Data;

@Data
public class Question {
    private String content;

    public enum Category {
        POP, SCIENCE, SPORTS, ROCK
    }

    private Category category;

    public Question(Category category, String content) {
        this.category = category;
        this.content = content;
    }

}
