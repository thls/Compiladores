package br.ufpe.cin.if688.minijava;

import br.ufpe.cin.if688.minijava.antlr.*;
import br.ufpe.cin.if688.minijava.ast.Program;
import br.ufpe.cin.if688.minijava.visitor.BuildSymbolTableVisitor;
import br.ufpe.cin.if688.minijava.visitor.MiniJavaVisitor;
import br.ufpe.cin.if688.minijava.visitor.PrettyPrintVisitor;
import br.ufpe.cin.if688.minijava.visitor.TypeCheckVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        // Creating AST
        List<String> l = new ArrayList<String>();
        l.add("Ball");
        l.add("TennisBal");
        l.add("TennisBalFew");
        l.add("LinkedList");
        l.add("Teleport");
        l.add("BalIdentifier");
        l.add("Shape");
        l.add("BallAlready");
        l.add("Factorial");
        l.add("LightSpeed");


//        for (String s : l){
//            System.out.println(">>>>" + s);
                Program program = (Program) new MiniJavaVisitor().visit(new MiniJavaGrammarParser(
                    new CommonTokenStream(new MiniJavaGrammarLexer(CharStreams.fromFileName("/home/CIN/thls/Compiladores/atividade5/" +
                            "MiniJavaAST/src/main" +
                    "/java/br/ufpe/cin/if688/minijava/in/i/LightSpeed"+".txt")))
            ).goal());
                BuildSymbolTableVisitor buildSymbolTableVisitor = new BuildSymbolTableVisitor();
            buildSymbolTableVisitor.visit(program);

            System.out.println(buildSymbolTableVisitor.getSymbolTable());
            TypeCheckVisitor typeCheckVisitor = new TypeCheckVisitor(buildSymbolTableVisitor.getSymbolTable());
            typeCheckVisitor.visit(program);
            System.out.println("--------------------------\n");
//        }


//        PrettyPrintVisitor p = new PrettyPrintVisitor();
//        p.visit(program);
        // Activity 5 test - Building SymbolTable and checking types



    }
}