package br.ufpe.cin.if688.parsing.analysis;

import br.ufpe.cin.if688.parsing.grammar.*;

import javax.swing.text.html.HTMLDocument;
import java.util.*;

import static br.ufpe.cin.if688.parsing.analysis.SpecialSymbol.EOF;
import static br.ufpe.cin.if688.parsing.analysis.SpecialSymbol.EPSILON;


public final class SetGenerator {

    public static Map<Nonterminal, Set<GeneralSymbol>> getFirst(Grammar g) {

        if (g == null) throw new NullPointerException("g nao pode ser nula.");

        Map<Nonterminal, Set<GeneralSymbol>> first = initializeNonterminalMapping(g);

        /*Calculando First*/

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
                    if (currentNonterminal.equals(production.getNonterminal())) {
                        Set<GeneralSymbol> auxFirst = new HashSet<GeneralSymbol>();

                        for (GeneralSymbol symbol : production.getProduction()) {
                            auxFirst.remove(EPSILON);

                            if ((symbol instanceof SpecialSymbol) || ((Symbol) symbol).isTerminal()) {
                                auxFirst.add(symbol);
                                break;
                            } else if (!(first.get(symbol).isEmpty())) {
                                if (!(first.get(symbol).contains(EPSILON))) {
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

        /*Fim*/

        return sortList(first);

    }


    public static Map<Nonterminal, Set<GeneralSymbol>> getFollow(Grammar g, Map<Nonterminal, Set<GeneralSymbol>> first) {

        if (g == null || first == null)
            throw new NullPointerException();

        Map<Nonterminal, Set<GeneralSymbol>> follow = initializeNonterminalMapping(g);


        /*Minha implementação*/


        List nonterminals = new ArrayList(g.getNonterminals());
        Collection<Production> productions = g.getProductions();
        boolean repeat = false;
        boolean breach = false;
        do {
            int start = nonterminals.indexOf(g.getStartSymbol());
            if (repeat) {
                start = 0;
            }
            repeat = false;


            for (; start < nonterminals.size(); start++){
                breach = false;
                Nonterminal currentNonterminal = (Nonterminal) nonterminals.get(start);
                Set<GeneralSymbol> currentFollow = new HashSet<GeneralSymbol>();

                for (Production production : productions){
                    List <GeneralSymbol> symbols = production.getProduction();

                    if (symbols.contains(currentNonterminal)){

                        for (int i = symbols.indexOf(currentNonterminal) + 1; i <= symbols.size();i++){
                            if (i < symbols.size()){
                                if ( (symbols.get(i) instanceof Symbol) && (((Symbol) symbols.get(i)).isTerminal())) {
                                    currentFollow.add(symbols.get(i));
                                    break;
                                } else if ( !(first.get(symbols.get(i)).contains(EPSILON))){
                                   currentFollow.addAll(first.get(symbols.get(i)));
                                   break;
                                } else{
                                    currentFollow.addAll(first.get(symbols.get(i)));
                                    currentFollow.remove(EPSILON);
                                }
                            }else if(!(follow.get(production.getNonterminal()).isEmpty())){
                                currentFollow.addAll(follow.get(production.getNonterminal()));
                            } else{
                                breach = true;
//                                repeat = true;
                                break;
                            }
                        }
                    }
                    if (breach){
                        break;
                    }
                }
                follow.put(currentNonterminal, currentFollow);
                if (currentNonterminal.equals(g.getStartSymbol())){
                    follow.get(currentNonterminal).add(EOF);
                }

            }

        for (int j = 0; j < nonterminals.size(); j++){
            if (follow.get(nonterminals.get(j)).isEmpty()){
                repeat = true;
            }
        }
        } while (repeat);

        /*Fim*/
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