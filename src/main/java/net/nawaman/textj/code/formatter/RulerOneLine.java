package net.nawaman.textj.code.formatter;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Ruler generator with one line.
 */
public class RulerOneLine implements RulerGenerator {
    
    /** The one line ruler. */
    public static final RulerGenerator oneLineRuler = new RulerOneLine();
    
    private static final String columnRuler
            = "----+---10----+---20----+---30----+---40----+---50----+---60----+---70----+---80----+---90----+--100"
            + "----+--110----+--120----+--130----+--140----+--150----+--160----+--170----+--180----+--190----+--200"
            + "----+--210----+--220----+--230----+--240----+--250----+--260----+--270----+--280----+--290----+--300"
            + "----+--310----+--320----+--330----+--340----+--350----+--360----+--370----+--380----+--390----+--400"
            + "----+--410----+--420----+--430----+--440----+--450----+--460----+--470----+--480----+--490----+--500"
            + "----+--510----+--520----+--530----+--540----+--550----+--560----+--570----+--580----+--590----+--600"
            + "----+--610----+--620----+--630----+--640----+--650----+--660----+--670----+--680----+--690----+--700"
            + "----+--710----+--720----+--730----+--740----+--750----+--760----+--770----+--780----+--790----+--800"
            + "----+--810----+--820----+--830----+--840----+--850----+--860----+--870----+--880----+--890----+--900"
            + "----+--910----+--920----+--930----+--940----+--950----+--960----+--970----+--980----+--990----+-1000";
    
    private final ConcurrentHashMap<Integer, String> columnCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, String> prefixCache = new ConcurrentHashMap<>();
    
    @Override
    public void addRuler(StringBuilder buffer, String linePrefix, int lineWidth) {
        var width = Math.min(lineWidth, columnRuler.length());
        
        buffer
        .append(prefixCache.computeIfAbsent(linePrefix.length(), __ -> linePrefix.replaceAll(" ", "-")))
        .append(columnCache.computeIfAbsent(lineWidth, __ -> columnRuler.substring(0, width)));
    }
    
}
