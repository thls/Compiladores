package br.ufpe.cin.if688.parsing.analysis;

import br.ufpe.cin.if688.parsing.grammar.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Comparator;
import java.util.Collections;
import java.util.HashMap;

import static br.ufpe.cin.if688.parsing.analysis.SpecialSymbol.EOF;
import static br.ufpe.cin.if688.parsing.analysis.SpecialSymbol.EPSILON;


public final class SetGenerator {

    public static Map<Nonterminal, Set<GeneralSymbol>> getFirst(Grammar g) {

        if (g == null) throw new NullPointerException("g nao pode ser nula.");

        Map<Nonterminal, Set<GeneralSymbol>> first = initializeNonterminalMapping(g);
        /*
         * Implemente aqui o método para retornar o conjunto first
         */


        return sortList(first);

    }


    public static Map<Nonterminal, Set<GeneralSymbol>> getFollow(Grammar g, Map<Nonterminal, Set<GeneralSymbol>> first) {

        if (g == null || first == null)
            throw new NullPointerException();

        Map<Nonterminal, Set<GeneralSymbol>> follow = initializeNonterminalMapping(g);

        /*
         * implemente aqui o método para retornar o conjunto follow
         */

        return sortList(follow);
    }

    static private Map<Nonterminal, Set<GeneralSymbol>> sortList(Map<Nonterminal, Set<GeneralSymbol>> firstOrFollow){

        Map<Nonterminal, Set<GeneralSymbol>> sortedList = new TreeMap<Nonterminal, Set<GeneralSymbol>>(new Comparator<Nonterminal>() {
            @Override
            public int compare(Nonterminal o1, Nonterminal o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });

        firstOrFollow.forEach((k,v)->{
            List<GeneralSymbol> list = new ArrayList<>(v);
            Collections.sort(list, new Comparator<GeneralSymbol>() {
                @Override
                public int compare(GeneralSymbol t1, GeneralSymbol t2) {
                    return t1.toString().compareTo(t2.toString());
                }
            });
            Set<GeneralSymbol> sortedSet = new HashSet<>();
            for (GeneralSymbol gs : list){
                sortedSet.add(gs);
            }
            sortedList.put(k, sortedSet);
        });

        Map<Nonterminal, Set<GeneralSymbol>> sortedMap = new TreeMap<>(new Comparator<Nonterminal>() {
            @Override
            public int compare(Nonterminal t1, Nonterminal t2) {
                return t1.toString().compareTo(t2.toString());
            }
        });

        sortedMap = sortedList;

        return sortedMap;
    }

    //método para inicializar mapeamento nãoterminais -> conjunto de símbolos
    private static Map<Nonterminal, Set<GeneralSymbol>>
    initializeNonterminalMapping(Grammar g) {
        Map<Nonterminal, Set<GeneralSymbol>> result =
                new HashMap<Nonterminal, Set<GeneralSymbol>>();

        for (Nonterminal nt: g.getNonterminals())
            result.put(nt, new HashSet<GeneralSymbol>());

        return result;
    }

}