public class SerializableMat {
    private String serializedBytes;
    private String serializedFloats;
    private String serializedDoubles;
    private String serializedInts;

    private int type;
    private int rows;
    private int cols;

    public String getInts()
    {
        return serializedInts;
    }

    public void setInts(String serial)
    {
        this.serializedInts = serial;
    }

    public String getDoubles()
    {
        return serializedDoubles;
    }

    public void setDoubles(String serial)
    {
        this.serializedDoubles = serial;
    }

    public String getBytes()
    {
        return serializedBytes;
    }

    public void setBytes(String serial)
    {
        this.serializedBytes = serial;
    }

    public String getFloats()
    {
        return serializedFloats;
    }

    public void setFloats(String serial)
    {
        this.serializedFloats = serial;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public int getRows()
    {
        return rows;
    }

    public void setRows(int rows)
    {
        this.rows = rows;
    }

    public int getCols()
    {
        return cols;
    }

    public void setCols(int cols)
    {
        this.cols = cols;
    }
}
