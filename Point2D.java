public record Point2D(double x, double y) {

    // Equals and hashCode methods
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Point2D(double x1, double y1))) return false;
        return Double.compare(x, x1) == 0 && Double.compare(y, y1) == 0;
    }

    // toString method
    @Override
    public String toString() { return "(" + x + ", " + y + ")"; }
}