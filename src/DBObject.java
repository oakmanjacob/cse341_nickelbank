public interface DBObject {

    String table_name = null;

    /**
     * Save current values of object to database
     * @return success
     */
    public int save();

    /**
     * Delete record from the database
     * @return success?
     */
    public int delete();
}
