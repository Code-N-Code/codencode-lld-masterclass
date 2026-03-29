package stackoverflow.model;

public class Tag {
    private final String name;

    public Tag(String name) {
        this.name = name.toLowerCase();
    }

    public String getName() { return name; }

    @Override
    public int hashCode() { return name.hashCode(); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Tag tag = (Tag) obj;
        return name.equals(tag.name);
    }
}
