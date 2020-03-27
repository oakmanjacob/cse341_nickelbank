package dao;

public interface DBObject {

    /**
     * Save current values of object to database
     * @return success
     */
    public boolean save();

    /**
     * Delete record from the database
     * @return success?
     */
    public boolean delete();
}
