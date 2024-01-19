public interface Background {

    public Tile getTile(int col, int row);

    public int getCol(double x);

    public int getRow(double y);

    public double getShiftX();

    public void setShiftX(double shiftX);

    public double getShiftY();

    public void setShiftY(double shiftY);

}
