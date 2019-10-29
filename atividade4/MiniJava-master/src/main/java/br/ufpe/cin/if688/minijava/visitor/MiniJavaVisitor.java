package br.ufpe.cin.if688.minijava.visitor;

import br.ufpe.cin.if688.minijava.antlr.*;
import br.ufpe.cin.if688.minijava.ast.*;
import com.sun.jdi.event.StepEvent;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import javax.swing.plaf.nimbus.State;
import java.util.List;

public class MiniJavaVisitor implements MiniJavaGrammarVisitor {
    @Override
    public Object visitGoal(MiniJavaGrammarParser.GoalContext ctx) {
        MainClass mc = (MainClass) ctx.mainClass().accept(this);
        ClassDeclList cdl = new ClassDeclList();
        for (MiniJavaGrammarParser.ClassDeclarationContext cdc: ctx.classDeclaration()){
            cdl.addElement((ClassDecl)cdc.accept(this));
        }
        return new Program(mc, cdl);
    }

    @Override
    public Object visitMainClass(MiniJavaGrammarParser.MainClassContext ctx) {

        Statement stm = (Statement) ctx.statement().accept(this);
        Identifier id1 = (Identifier) ctx.identifier().get(0).accept(this);
        Identifier id2 = (Identifier) ctx.identifier().get(1).accept(this);
        return new MainClass(id1, id2, stm);
    }

    @Override
    public Object visitClassDeclaration(MiniJavaGrammarParser.ClassDeclarationContext ctx) {
        if (ctx.getTokens(MiniJavaGrammarParser.T__12).size() != 0){
            Identifier id1 = (Identifier) ctx.identifier().get(0).accept(this);
            Identifier id2 = (Identifier) ctx.identifier().get(1).accept(this);
            VarDeclList vdl = new VarDeclList();
            for (MiniJavaGrammarParser.VarDeclarationContext vdc: ctx.varDeclaration()){
                vdl.addElement((VarDecl) vdc.accept(this));
            }
            MethodDeclList mdl = new MethodDeclList();
            for (MiniJavaGrammarParser.MethodDeclarationContext mdc : ctx.methodDeclaration()){
                mdl.addElement((MethodDecl) mdc.accept(this));
            }
            return new ClassDeclExtends(id1, id2, vdl, mdl);

        }else{
            Identifier id1 = (Identifier) ctx.identifier().get(0).accept(this);
            VarDeclList vdl = new VarDeclList();
            for (MiniJavaGrammarParser.VarDeclarationContext vdc: ctx.varDeclaration()){
                vdl.addElement((VarDecl) vdc.accept(this));
            }
            MethodDeclList mdl = new MethodDeclList();
            for (MiniJavaGrammarParser.MethodDeclarationContext mdc : ctx.methodDeclaration()){
                mdl.addElement((MethodDecl) mdc.accept(this));
            }
            return new ClassDeclSimple(id1, vdl, mdl);
        }
    }

    @Override
    public Object visitVarDeclaration(MiniJavaGrammarParser.VarDeclarationContext ctx) {

        if (ctx.statement() != null){
//            Statement stm = (Statement) ctx.statement().accept(this) ;
//            Identifier id = new Identifier()
        }else{
            Type t = (Type) ctx.type().accept(this);
            Identifier id = (Identifier) ctx.identifier().accept(this);
            return new VarDecl(t, id);
        }
        return null;
    }

    @Override
    public Object visitMethodDeclaration(MiniJavaGrammarParser.MethodDeclarationContext ctx) {
        Type t = (Type) ctx.type().get(0).accept(this);
        Identifier id = (Identifier) ctx.identifier().get(0).accept(this);
        FormalList fl = new FormalList();
        for (int i = 1; i < ctx.type().size(); i++){
            Type ft = (Type) ctx.type().get(i).accept(this);
            Identifier ai = (Identifier) ctx.identifier(i).accept(this);
            fl.addElement(new Formal(ft,ai));
        }
        VarDeclList vdl = new VarDeclList();
        for (MiniJavaGrammarParser.VarDeclarationContext vdc : ctx.varDeclaration()){
            vdl.addElement((VarDecl) vdc.accept(this));
        }
        StatementList sl = new StatementList();
        for (MiniJavaGrammarParser.StatementContext sc : ctx.statement()){
            sl.addElement((Statement) sc.accept(this));
        }
        Exp e = (Exp) ctx.expression().accept(this);

        return new MethodDecl(t, id, fl, vdl, sl, e);
    }

    @Override
    public Object visitStatement(MiniJavaGrammarParser.StatementContext ctx) {
        if (ctx.identifier() == null){
            if (ctx.getTokens(MiniJavaGrammarParser.T__19).size() != 0){
                Exp e = (Exp) ctx.expression().get(0).accept(this);
               return new Print(e);
            }else if (ctx.getTokens(MiniJavaGrammarParser.T__18).size() != 0){
                Exp e = (Exp) ctx.expression().get(0).accept(this);
                Statement stm = (Statement) ctx.statement().get(0).accept(this);
                return new While(e, stm);
            }else if (ctx.getTokens(MiniJavaGrammarParser.T__16).size() != 0){
                Exp e = (Exp) ctx.expression().get(0).accept(this);
                Statement stm1 = (Statement) ctx.statement().get(0).accept(this);
                Statement stm2 = (Statement) ctx.statement().get(1).accept(this);
                return new If(e, stm1, stm2);
            }else if (ctx.getTokens(MiniJavaGrammarParser.T__1).size() != 0){
                StatementList sl = new StatementList();
                for (MiniJavaGrammarParser.StatementContext sc : ctx.statement()){
                    sl.addElement((Statement)sc.accept(this));
                }
                return new Block (sl);
            }

        }else if (ctx.expression().size() < 2){
            Exp e = (Exp) ctx.expression().get(0).accept(this);
            Identifier id = (Identifier) ctx.identifier().accept(this);
            return new Assign(id, e);
        }else{
            Exp e1 = (Exp) ctx.expression().get(0).accept(this);
            Exp e2 = (Exp) ctx.expression().get(1).accept(this);
            Identifier id = (Identifier) ctx.identifier().accept(this);
            return new ArrayAssign(id, e1, e2);
        }
        return null;
    }

    @Override  //tirar print
    public Object visitExpression(MiniJavaGrammarParser.ExpressionContext ctx) {
        int a = ctx.expression().size();
        String b = ctx.getText();
        for (MiniJavaGrammarParser.ExpressionContext e : ctx.expression()){
            String c = e.getText();
            int aaa = 0;
        }
        if (ctx.expression().size() == 0){
            if (ctx.getTokens(MiniJavaGrammarParser.INTEGER_LITERAL).size() != 0){
                int i = Integer.parseInt(ctx.getTokens(MiniJavaGrammarParser.INTEGER_LITERAL).get(0).getText());
                return new IntegerLiteral(i);
            }else if (ctx.identifier() != null){
                if (ctx.getTokens(MiniJavaGrammarParser.T__31).size() != 0){
                    Identifier id = (Identifier) ctx.identifier().accept(this);
                    return new NewObject(id);
                }else{
                    Identifier id = (Identifier) ctx.identifier().accept(this);
                    return new IdentifierExp(id.s);
                }
            }else if (ctx.getTokens(MiniJavaGrammarParser.T__28).size() != 0){
                return new True();
            }else if (ctx.getTokens(MiniJavaGrammarParser.T__29).size() != 0){
                return new False();
            }else if (ctx.getTokens(MiniJavaGrammarParser.T__30).size() != 0){
                return new This();
            }
        }else if (ctx.expression().size() == 1){
            if (ctx.getTokens(MiniJavaGrammarParser.T__27).size() != 0){
                Exp e = (Exp) ctx.expression().get(0).accept(this);
                return new ArrayLength(e);
            }else if (ctx.getTokens(MiniJavaGrammarParser.T__8).size() != 0){
                Exp e = (Exp) ctx.expression().get(0).accept(this);
                return new NewArray(e);
            }else if(ctx.getTokens(MiniJavaGrammarParser.T__33).size() != 0){
                Exp e = (Exp) ctx.expression().get(0).accept(this);
                return new Not(e);
            }else if (ctx.identifier() != null){
                Exp e1 = (Exp) ctx.expression().get(0).accept(this);
                ExpList el = new ExpList();
                Identifier id = (Identifier) ctx.identifier().accept(this);
                return new Call(e1, id, el);
            }else{
                return ctx.expression().get(0).accept(this);
            }

        }else if (ctx.expression().size() == 2){
            if (ctx.getTokens(MiniJavaGrammarParser.T__21).size() != 0){
                Exp e1 = (Exp) ctx.expression().get(0).accept(this);
                Exp e2 = (Exp) ctx.expression().get(1).accept(this);
                return new And(e1, e2);
            }else if (ctx.getTokens(MiniJavaGrammarParser.T__22).size() != 0){
                Exp e1 = (Exp) ctx.expression().get(0).accept(this);
                Exp e2 = (Exp) ctx.expression().get(1).accept(this);
                return new LessThan(e1, e2);
            }else if (ctx.getTokens(MiniJavaGrammarParser.T__23).size() != 0){
                Exp e1 = (Exp) ctx.expression().get(0).accept(this);
                Exp e2 = (Exp) ctx.expression().get(1).accept(this);
                return new Plus(e1, e2);
            }else if (ctx.getTokens(MiniJavaGrammarParser.T__24).size() != 0){
                Exp e1 = (Exp) ctx.expression().get(0).accept(this);
                Exp e2 = (Exp) ctx.expression().get(1).accept(this);
                return new Minus(e1, e2);
            }else if (ctx.getTokens(MiniJavaGrammarParser.T__25).size() != 0){
                Exp e1 = (Exp) ctx.expression().get(0).accept(this);
                Exp e2 = (Exp) ctx.expression().get(1).accept(this);
                return new Times(e1, e2);
            }else if (ctx.getTokens(MiniJavaGrammarParser.T__9).size() != 0) {
                Exp e1 = (Exp) ctx.expression().get(0).accept(this);
                Exp e2 = (Exp) ctx.expression().get(1).accept(this);
                return new ArrayLookup(e1, e2);
            }else {
                Exp e1 = (Exp) ctx.expression().get(0).accept(this);
                Exp e2 = (Exp) ctx.expression().get(1).accept(this);
                ExpList el = new ExpList();
                el.addElement(e2);
                Identifier id = (Identifier) ctx.identifier().accept(this);
                return new Call(e1, id, el);
            }
        }else {
            Exp e1 = (Exp) ctx.expression().get(0).accept(this);
            ExpList el = new ExpList();
            for (int i = 1; i < ctx.expression().size(); i++){
                el.addElement((Exp) ctx.expression(i).accept(this));
            }
            Identifier id = (Identifier) ctx.identifier().accept(this);
            return new Call (e1, id, el);
        }
        return null;
    }

    @Override
    public Object visitType(MiniJavaGrammarParser.TypeContext ctx) {
        if (ctx.getTokens(MiniJavaGrammarParser.T__8).size() != 0){
            return new IntArrayType();
        }else if(ctx.getTokens(MiniJavaGrammarParser.T__34).size() != 0){
            return new BooleanType();
        }else if(ctx.getTokens(MiniJavaGrammarParser.T__32).size() != 0){
            return new IntegerType();
        }else {
            Identifier id = (Identifier) ctx.identifier().accept(this);
            return new IdentifierType(id.s);
        }
    }

    @Override
    public Object visitIdentifier(MiniJavaGrammarParser.IdentifierContext ctx) {
        return new Identifier(ctx.IDENTIFIER().getText());
    }

    @Override
    public Object visit(ParseTree parseTree) {
        Program program = (Program) parseTree.accept(this);
        return program;
    }

    @Override
    public Object visitChildren(RuleNode ruleNode) {
        return null;
    }

    @Override
    public Object visitTerminal(TerminalNode terminalNode) {
        return null;
    }

    @Override
    public Object visitErrorNode(ErrorNode errorNode) {
        return null;
    }


}
