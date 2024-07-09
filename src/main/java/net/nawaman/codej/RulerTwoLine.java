package net.nawaman.codej;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Ruler generator with two lines.
 */
public class RulerTwoLine implements RulerGenerator {
    
    /** The top-line two line ruler. */
    public static final RulerGenerator topTwoLineRuler    = new RulerTwoLine(true);
    
    /** The bottom-line two line ruler. */
    public static final RulerGenerator bottomTwoLineRuler = new RulerTwoLine(false);
    
    private static final String columnRuler
            = "        10        20        30        40        50        60        70        80        90       100"
            + "       110       120       130       140       150       160       170       180       190       200"
            + "       210       220       230       240       250       260       270       280       290       300"
            + "       310       320       330       340       350       360       370       380       390       400"
            + "       410       420       430       440       450       460       470       480       490       500"
            + "       510       520       530       540       550       560       570       580       590       600"
            + "       610       620       630       640       650       660       670       680       690       700"
            + "       710       720       730       740       750       760       770       780       790       800"
            + "       810       820       830       840       850       860       870       880       890       900"
            + "       910       920       930       940       950       960       970       980       990      1000";
    
    private static final String lineRuler
            = "----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|"
            + "----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|"
            + "----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|"
            + "----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|"
            + "----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|"
            + "----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|"
            + "----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|"
            + "----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|"
            + "----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|"
            + "----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|";
    
    private final ConcurrentHashMap<Integer, String> columnCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, String> lineCache   = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, String> prefixCache = new ConcurrentHashMap<>();
    
    private final boolean isTop;
    
    /** Construct a RuleTwoLine ruler generator. */
    public RulerTwoLine(boolean isTop) {
        this.isTop = isTop;
    }
    
    @Override
    public void addRuler(StringBuilder buffer, String prefix, int width) {
        if (isTop) {
            appendColumns(buffer, prefix, width);
            buffer.append("\n");
            
            appendLine(buffer, prefix, width);
        } else {
            appendLine(buffer, prefix, width);
            buffer.append("\n");
            
            appendColumns(buffer, prefix, width);
        }
    }   
    
    private void appendColumnPrefix(StringBuilder buffer, String linePrefix) {
        buffer.append(linePrefix);
    }
    
    private void appendColumns(StringBuilder buffer, String linePrefix, int lineWidth) {
        appendColumnPrefix(buffer, linePrefix);
        buffer.append(columnCache.computeIfAbsent(lineWidth, __ -> columnRuler.substring(0, lineWidth)));
    }
    
    private void appendLinePrefix(StringBuilder buffer, String linePrefix) {
        buffer.append(prefixCache.computeIfAbsent(linePrefix.length(), __ -> linePrefix.replaceAll(" ", "-")));
    }
    
    private void appendLine(StringBuilder buffer, String linePrefix, int lineWidth) {
        appendLinePrefix(buffer, linePrefix);
        buffer.append(lineCache  .computeIfAbsent(lineWidth, __ -> lineRuler.substring(0, lineWidth)));
    }
    
}
