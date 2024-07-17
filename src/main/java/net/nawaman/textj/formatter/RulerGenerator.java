package net.nawaman.textj.formatter;

/**
 * Generator for code ruler.
 */
public interface RulerGenerator {
    
    /**
     * Add a code ruler to the string builder.
     * 
     * @param buffer      the string builder to add the ruler to.
     * @param linePrefix  the prefix to add to the ruler.
     * @param lineWidth   the width of the ruler.
     */
    public void addRuler(StringBuilder buffer, String linePrefix, int lineWidth);
    
}
