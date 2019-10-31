package br.ufpe.cin.if688.minijava.visitor;

import br.ufpe.cin.if688.minijava.ast.*;
import br.ufpe.cin.if688.minijava.exceptions.PrintException;
import br.ufpe.cin.if688.minijava.symboltable.Class;
import br.ufpe.cin.if688.minijava.symboltable.Method;
import br.ufpe.cin.if688.minijava.symboltable.SymbolTable;

import java.util.Iterator;

import java.util.function.BinaryOperator;

public class TypeCheckVisitor implements IVisitor<Type> {

	private SymbolTable symbolTable;

	Class currClass;
	Method currMethod;


	public TypeCheckVisitor(SymbolTable st) {
		symbolTable = st;
	}

	// MainClass m;
	// ClassDeclList cl;
	public Type visit(Program n) {
		n.m.accept(this);
		for (int i = 0; i < n.cl.size(); i++) {
			n.cl.elementAt(i).accept(this);
		}
		return null;
	}

	// Identifier i1,i2;
	// Statement s;
	public Type visit(MainClass n) {
		currClass = symbolTable.getClass(n.i1.toString());
		n.i1.accept(this);
		n.i2.accept(this);
		n.s.accept(this);
		return null;
	}

	// Identifier i;
	// VarDeclList vl;
	// MethodDeclList ml;
	public Type visit(ClassDeclSimple n) {
		currClass = symbolTable.getClass(n.i.toString());
		n.i.accept(this);
		for (int i = 0; i < n.vl.size(); i++) {
			n.vl.elementAt(i).accept(this);
		}
		for (int i = 0; i < n.ml.size(); i++) {
			currMethod = currClass.getMethod(n.ml.elementAt(i).i.toString());
			n.ml.elementAt(i).accept(this);
		}
		currMethod = null;
		return null;
	}

	// Identifier i;
	// Identifier j;
	// VarDeclList vl;
	// MethodDeclList ml;
	public Type visit(ClassDeclExtends n) {
		currClass = symbolTable.getClass(n.i.toString());
		n.i.accept(this);
		if (!symbolTable.containsClass(n.j.toString())){
			PrintException.idNotFound(n.j.toString());
		}
		n.j.accept(this);
		for (int i = 0; i < n.vl.size(); i++) {
			n.vl.elementAt(i).accept(this);
		}
		for (int i = 0; i < n.ml.size(); i++) {
			currMethod = currClass.getMethod(n.ml.elementAt(i).i.toString());
			n.ml.elementAt(i).accept(this);
		}
		currMethod = null;
		return null;
	}

	// Type t;
	// Identifier i;
	public Type visit(VarDecl n) {
		n.t.accept(this);
		n.i.accept(this);
		return null;
	}

	// Type t;
	// Identifier i;
	// FormalList fl;
	// VarDeclList vl;
	// StatementList sl;
	// Exp e;
	public Type visit(MethodDecl n) {
		n.t.accept(this);
		n.i.accept(this);
		for (int i = 0; i < n.fl.size(); i++) {
			n.fl.elementAt(i).accept(this);
		}
		for (int i = 0; i < n.vl.size(); i++) {
			n.vl.elementAt(i).accept(this);
		}
		for (int i = 0; i < n.sl.size(); i++) {
			n.sl.elementAt(i).accept(this);
		}
		n.e.accept(this);
		return null;
	}

	// Type t;
	// Identifier i;
	public Type visit(Formal n) {
		n.t.accept(this);
		n.i.accept(this);
		return null;
	}

	public Type visit(IntArrayType n) {
		return new IntegerType();
	}

	public Type visit(BooleanType n) {
		return n;
	}

	public Type visit(IntegerType n) {
		return n;
	}

	// String s;
	public Type visit(IdentifierType n) {
		return n;
	}

	// StatementList sl;
	public Type visit(Block n) {
		for (int i = 0; i < n.sl.size(); i++) {
			n.sl.elementAt(i).accept(this);
		}
		return null;
	}

	// Exp e;
	// Statement s1,s2;
	public Type visit(If n) {
		Type t = n.e.accept(this);
		if (!(t instanceof BooleanType)){
			PrintException.typeMatch(new BooleanType(), t);
		}
		n.s1.accept(this);
		n.s2.accept(this);
		return null;
	}

	// Exp e;
	// Statement s;
	public Type visit(While n) {
		Type t = n.e.accept(this);
		if (!(t instanceof BooleanType)){
			PrintException.typeMatch(new BooleanType(), t);
		}
		n.s.accept(this);
		return null;
	}

	// Exp e;
	public Type visit(Print n) {
		n.e.accept(this);
		return null;
	}

	// Identifier i;
	// Exp e;
	public Type visit(Assign n) {
		Type id = n.i.accept(this);
		Type exp = n.e.accept(this);
		if (!(symbolTable.compareTypes(id, exp))){
			PrintException.typeMatch(id, exp);
		}
		return null;
	}

	// Identifier i;
	// Exp e1,e2;
	public Type visit(ArrayAssign n) {
		Type id = n.i.accept(this);
		Type inte = n.e1.accept(this);
		Type obj = n.e2.accept(this);
		if (!(symbolTable.compareTypes(new IntegerType(), inte))){
			PrintException.typeMatch(new IntegerType(), inte);
		} else if (!((id instanceof IntegerType))){
			PrintException.typeMatch(id, obj);
		}
		return null;
	}

	// Exp e1,e2;
	public Type visit(And n) {
		Type t = n.e1.accept(this);
		Type t2 = n.e2.accept(this);
		if (! (t instanceof BooleanType)){
			PrintException.typeMatch(new BooleanType(), t);
		}else if (!(t2 instanceof  BooleanType)){
			PrintException.typeMatch(new BooleanType(), t2);
		}
		return new BooleanType();
	}

	// Exp e1,e2;
	public Type visit(LessThan n) {
		Type t = n.e1.accept(this);
		Type i = n.e2.accept(this);
		if (! (t instanceof IntegerType)){
			PrintException.typeMatch(new IntegerType(), t);
		}else if (!(i instanceof  IntegerType)){
			PrintException.typeMatch(new IntegerType(), i);
		}
		return new BooleanType();
	}

	// Exp e1,e2;
	public Type visit(Plus n) {
		Type t = n.e1.accept(this);
		Type i = n.e2.accept(this);
		if (! (t instanceof IntegerType)){
			PrintException.typeMatch(new IntegerType(), t);
		}else if (!(i instanceof  IntegerType)){
			PrintException.typeMatch(new IntegerType(), i);
		}
		return new IntegerType();
	}

	// Exp e1,e2;
	public Type visit(Minus n) {
		Type t = n.e1.accept(this);
		Type i = n.e2.accept(this);
		if (! (t instanceof IntegerType)){
			PrintException.typeMatch(new IntegerType(), t);
		}else if (!(i instanceof  IntegerType)){
			PrintException.typeMatch(new IntegerType(), i);
		}
		return new IntegerType();
	}

	// Exp e1,e2;
	public Type visit(Times n) {
		Type t = n.e1.accept(this);
		Type i = n.e2.accept(this);
		if (! (t instanceof IntegerType)){
			PrintException.typeMatch(new IntegerType(), t);
		}else if (!(i instanceof  IntegerType)){
			PrintException.typeMatch(new IntegerType(), i);
		}
		return new IntegerType();
	}

	// Exp e1,e2;
	public Type visit(ArrayLookup n) {
		Type id = n.e1.accept(this);
		Type i = n.e2.accept(this);
		if (! (i instanceof IntegerType)){
			PrintException.typeMatch(new IntegerType(), i);
		}

		return id;
	}

	// Exp e;
	public Type visit(ArrayLength n) {
		n.e.accept(this);
		return new IntegerType();
	}

	// Exp e;
	// Identifier i;
	// ExpList el;
	public Type visit(Call n) {
		if (n.e instanceof NewObject) {
			Method m = symbolTable.getClass(((NewObject) n.e).i.s).getMethod(n.i.toString());
			if (m == null) {
				PrintException.idNotFound(n.i.toString());
			} else {
				return callCheck(m, n);
			}
		}else if (n.e instanceof IdentifierExp || n.e instanceof This) {
			Type t = n.e.accept(this);
			if (t instanceof IdentifierType) {
				Method m = symbolTable.getClass(((IdentifierType) t).s).getMethod(n.i.toString());
				if (m == null){
					PrintException.idNotFound(((IdentifierType) t).s);
				}else{
					return callCheck(m, n);
				}

			}

		}
		return null;
	}

	// int i;
	public Type visit(IntegerLiteral n) {
		return new IntegerType();
	}

	public Type visit(True n) {
		return new BooleanType();
	}

	public Type visit(False n) {
		return new BooleanType();
	}

	// String s;
	public Type visit(IdentifierExp n) {
		if (currMethod == null){
			return currClass.getVar(n.s).type();
		}else{
			return symbolTable.getVarType(currMethod, currClass, n.s);
		}
	}

	public Type visit(This n) {
		return new IdentifierType(currClass.getId());
	}

	// Exp e;
	public Type visit(NewArray n) {
		Type i = n.e.accept(this);
		if (! (i instanceof IntegerType)){
			PrintException.typeMatch(new IntegerType(), i);
		}
		return null;
	}

	// Identifier i;
	public Type visit(NewObject n) {
		return new IdentifierType(n.i.toString());
	}

	// Exp e;
	public Type visit(Not n) {
		Type bool = n.e.accept(this);
		if (! (bool instanceof BooleanType)){
			PrintException.typeMatch(new BooleanType(), bool);
		}
		return new BooleanType();
	}

	// String s;
	public Type visit(Identifier n) {
//		if (symbolTable.getClass(n.toString()) != null){
//			return new IdentifierType(n.toString());
//		}else if (!var.containsKey(n.toString())){
//			PrintException.idNotFound(n.toString());
//
//		}else{
//			return var.get(n.toString());
//		}

		return var.get(n.toString());
	}

	//AuxFunction
	public Type callCheck(Method m, Call n){

				Iterator iter = m.getParams().asIterator();
				int size = 0;
				while (iter.hasNext()){
					iter.next();
					size++;
				}
				if (size == n.el.size()){
					for (int i = 0; i < n.el.size(); i++) {
						Type t = n.el.elementAt(i).accept(this);
						if (!(symbolTable.compareTypes(t, m.getParamAt(i).type()))){
							PrintException.typeMatch(t, m.getParamAt(i).type());
						}
					}
					return m.type();
				}else if (size < n.el.size()){
					PrintException.tooManyArguments(m.getId());
				}else{
					PrintException.tooFewArguments(m.getId());
				}
		return null;
	}
}
