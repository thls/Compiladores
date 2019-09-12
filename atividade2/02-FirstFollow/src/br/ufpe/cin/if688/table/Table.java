package br.ufpe.cin.if688.table;


import br.ufpe.cin.if688.parsing.analysis.*;
import br.ufpe.cin.if688.parsing.grammar.*;

import java.util.*;

import static br.ufpe.cin.if688.parsing.analysis.SpecialSymbol.EPSILON;


public final class Table {
    private Table() {    }

    public static Map<LL1Key, List<GeneralSymbol>> createTable(Grammar g) throws NotLL1Exception {
        if (g == null) throw new NullPointerException();

        Map<Nonterminal, Set<GeneralSymbol>> first =
                SetGenerator.getFirst(g);
        Map<Nonterminal, Set<GeneralSymbol>> follow =
                SetGenerator.getFollow(g, first);

        Map<LL1Key, List<GeneralSymbol>> parsingTable =
                new HashMap<LL1Key, List<GeneralSymbol>>();



        /*Minha implementaçao*/


        Iterator nonterminals = (new ArrayList(g.getNonterminals())).iterator();
        Collection<Production> productions = g.getProductions();
        Collection<Terminal> terminals = g.getTerminals();

        while(nonterminals.hasNext()){
            Nonterminal currentNonterminal = (Nonterminal) nonterminals.next();

            if (!(first.get(currentNonterminal).contains(EPSILON))){

                for (GeneralSymbol symbol : first.get(currentNonterminal)){

                    for (Production production : productions){
                        if (production.getNonterminal().equals(currentNonterminal)){

                            for (GeneralSymbol g_symbol : production.getProduction()){
                                if ( ( (Symbol) g_symbol).isTerminal() && g_symbol.equals(symbol) ){
                                    parsingTable.put(new LL1Key(currentNonterminal, symbol), production.getProduction());
                                    break;
                                } else if (((Symbol) g_symbol).isTerminal()){
                                    break;
                                } else if ( (first.get(g_symbol).contains(symbol)) ){
                                    parsingTable.put(new LL1Key(currentNonterminal, symbol), production.getProduction());
                                    break;
                                } else if (!(first.get(g_symbol).contains(EPSILON))){
                                    break;
                                }
                            }
                        }
                    }
                }
            } else{
                for (GeneralSymbol symbol : first.get(currentNonterminal)){

                    for (Production production : productions){
                        if (production.getNonterminal().equals(currentNonterminal)){

                            for (GeneralSymbol g_symbol : production.getProduction()){
                                if (g_symbol instanceof SpecialSymbol){}
                                else if ( ( (Symbol) g_symbol).isTerminal() && g_symbol.equals(symbol) ){
                                    parsingTable.put(new LL1Key(currentNonterminal, symbol), production.getProduction());
                                    break;
                                } else if (((Symbol) g_symbol).isTerminal()){
                                    break;
                                } else if ( (first.get(g_symbol).contains(symbol)) ){
                                    parsingTable.put(new LL1Key(currentNonterminal, symbol), production.getProduction());
                                    break;
                                } else if (!(first.get(g_symbol).contains(EPSILON))){
                                    break;
                                }
                            }
                        }
                    }
                }
                List <GeneralSymbol> epsilon = new ArrayList();
                epsilon.add(EPSILON);
                for (GeneralSymbol symbol : follow.get(currentNonterminal)){
                    parsingTable.put( (new LL1Key(currentNonterminal, symbol)) , epsilon);
                }
            }
        }

        /*Fim*/
        // ------------------------------ Aqui já tem os prints para voce ------------

        System.out.print(g);
        System.out.println(first);
        System.out.println(follow);
        System.out.println(sortTable(parsingTable));

        return sortTable(parsingTable);
    }


    static private Map<LL1Key, List<GeneralSymbol>> sortTable( Map<LL1Key, List<GeneralSymbol>> parsingTable) {
        // This sorts only the key, as the values in the set must be in the order of the rule
        Map<LL1Key, List<GeneralSymbol>> sortedMap = new TreeMap<LL1Key, List<GeneralSymbol>>(new Comparator<LL1Key>() {
            @Override
            public int compare(LL1Key t1, LL1Key t2) {
                return t1.toString().compareTo(t2.toString());
            }
        });

        parsingTable.forEach((k,v)->{
            sortedMap.put(k, v);
        });

        return sortedMap;
    }
}