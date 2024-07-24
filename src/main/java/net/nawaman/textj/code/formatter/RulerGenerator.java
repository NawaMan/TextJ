package net.nawaman.textj.code.formatter;

/**
 * Generator for ruler.
 */
public interface RulerGenerator {
    
    /**
     * Add a ruler to the string builder.
     * 
     * @param buffer      the string builder to add the ruler to.
     * @param linePrefix  the prefix to add to the ruler.
     * @param lineWidth   the width of the ruler.
     */
    public void addRuler(StringBuilder buffer, String linePrefix, int lineWidth);
    
}
