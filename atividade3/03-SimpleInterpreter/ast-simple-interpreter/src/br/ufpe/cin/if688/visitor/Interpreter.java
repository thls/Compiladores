package br.ufpe.cin.if688.visitor;

import br.ufpe.cin.if688.ast.AssignStm;
import br.ufpe.cin.if688.ast.CompoundStm;
import br.ufpe.cin.if688.ast.EseqExp;
import br.ufpe.cin.if688.ast.Exp;
import br.ufpe.cin.if688.ast.ExpList;
import br.ufpe.cin.if688.ast.IdExp;
import br.ufpe.cin.if688.ast.LastExpList;
import br.ufpe.cin.if688.ast.NumExp;
import br.ufpe.cin.if688.ast.OpExp;
import br.ufpe.cin.if688.ast.PairExpList;
import br.ufpe.cin.if688.ast.PrintStm;
import br.ufpe.cin.if688.ast.Stm;
import br.ufpe.cin.if688.symboltable.Table;

public class Interpreter implements IVisitor<Table> {
	
	//a=8;b=80;a=7;
	// a->7 ==> b->80 ==> a->8 ==> NIL
	private Table t;
	boolean unAss = true;
	
	public Interpreter(Table t) {
		this.t = t;
	}

	@Override
	public Table visit(Stm s) {
		// TODO Auto-generated method stub
		t = new Table (null,0, null);
		return s.accept(this);
	}

	@Override
	public Table visit(AssignStm s) {
		// TODO Auto-generated method stub
		Table j = s.getExp().accept(this);
		if (unAss){
			t.id = s.getId();
			t.value = j.value;
			unAss = false;
		}else{
			t.tail = new Table (s.getId(), j.value, null);
		}
		return t;
	}

	@Override
	public Table visit(CompoundStm s) {
		// TODO Auto-generated method stub
		s.getStm1().accept(this);
		s.getStm2().accept(this);
		return null;
	}

	@Override
	public Table visit(PrintStm s) {
		// TODO Auto-generated method stub
		return s.getExps().accept(this);
	}

	@Override
	public Table visit(Exp e) {
		// TODO Auto-generated method stub
		e.accept(this);
		return null;
	}

	@Override
	public Table visit(EseqExp e) {
		// TODO Auto-generated method stub
		e.getStm().accept(this);
		return e.getExp().accept(this);
	}

	@Override
	public Table visit(IdExp e) {
		// TODO Auto-generated method stub
		Table tt = t;
//
		while (tt != null){
			if (tt.id == e.getId()){
				return tt;
			}
			tt = tt.tail;
		}
		return null;
	}

	@Override
	public Table visit(NumExp e) {
		// TODO Auto-generated method stub
		return new Table (null, e.getNum(), null);
	}

	@Override
	public Table visit(OpExp e) {
		// TODO Auto-generated method stub
		switch (e.getOper()){
			case 1:
				return new Table (null, e.getLeft().accept(this).value + e.getRight().accept(this).value, null);
			case 2:
				return new Table (null, e.getLeft().accept(this).value - e.getRight().accept(this).value, null);
			case 3:
				return new Table (null, e.getLeft().accept(this).value * e.getRight().accept(this).value, null);
			case 4:
				return new Table (null, e.getLeft().accept(this).value / e.getRight().accept(this).value, null);
		}
		return null;
	}

	@Override
	public Table visit(ExpList el) {
		// TODO Auto-generated method stub
		el.accept(this);
		return null;
	}

	@Override
	public Table visit(PairExpList el) {
		// TODO Auto-generated method stub
		System.out.println(el.getHead().accept(this).value);
		el.getTail().accept(this);
		return null;
	}

	@Override
	public Table visit(LastExpList el) {
		// TODO Auto-generated method stub
		System.out.println(el.getHead().accept(this).value);
		return null;
	}


}
