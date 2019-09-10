package br.ufpe.cin.if688.parsing.analysis;

import br.ufpe.cin.if688.parsing.grammar.*;

import java.util.*;

import static br.ufpe.cin.if688.parsing.analysis.SpecialSymbol.EOF;
import static br.ufpe.cin.if688.parsing.analysis.SpecialSymbol.EPSILON;


public final class SetGenerator {

    public static Map<Nonterminal, Set<GeneralSymbol>> getFirst(Grammar g) {

        if (g == null) throw new NullPointerException("g nao pode ser nula.");

        Map<Nonterminal, Set<GeneralSymbol>> first = initializeNonterminalMapping(g);

        List nonterminals = new ArrayList(g.getNonterminals());

        Collection<Production> productions = g.getProductions();

        boolean finished;
        boolean again;
        do{
            again = false;
            for (int i = 0; i < nonterminals.size();){
                finished = false;
                Set<GeneralSymbol> currentFirst = new HashSet<GeneralSymbol>();
                Nonterminal currentNonterminal = (Nonterminal) nonterminals.get(i);

                for (Production production : productions) {
                    finished = false;
                    if (currentNonterminal == production.getNonterminal()) {
                        Set<GeneralSymbol> auxFirst = new HashSet<GeneralSymbol>();

                        for (GeneralSymbol symbol : production.getProduction()) {
                            auxFirst.remove(SpecialSymbol.EPSILON);

                            if ((symbol instanceof SpecialSymbol) || ((Symbol) symbol).isTerminal()) {
                                auxFirst.add(symbol);
                                break;
                            } else if (!(first.get(symbol).isEmpty())) {
                                if (!(first.get(symbol).contains(SpecialSymbol.EPSILON))) {
                                    auxFirst.addAll(first.get(symbol));
                                    break;
                                } else {
                                    auxFirst.addAll(first.get(symbol));
                                }
                            } else {
                                finished = true;
                                again = true;
                                break;
                            }
                        }
                        currentFirst.addAll(auxFirst);
                    }
                    if (finished){
                        break;
                    }
                }
                if (!finished){
                    first.put(currentNonterminal, currentFirst);
                }
                i++;
            }

        } while (again);


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