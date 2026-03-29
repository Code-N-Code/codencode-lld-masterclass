package stackoverflow.dto;

import stackoverflow.model.Tag;

public class Filter {
    private Tag tag;
    private String authorId;

    private Filter() {}
    private Filter(Tag tag, String authorId) {
        this.tag = tag;
        this.authorId = authorId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public Tag getTag() {
        return tag;
    }

    public static class Builder {
        private Tag tag;
        private String authorId;

        public Builder tag(Tag tag) {
            this.tag = tag;
            return this;
        }

        public Builder userId(String authorId) {
            this.authorId = authorId;
            return this;
        }

        public Filter build() {
            return new Filter(tag, authorId);
        }
    }
}
