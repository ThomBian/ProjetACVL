package model;

public abstract class NamedState extends State {

    protected String name;

    public NamedState(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean isNamedState() {
        return true;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "[State " + name + " ]";
    }
}
