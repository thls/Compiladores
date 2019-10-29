grammar MiniJavaGrammar;

goal        :   mainClass(classDeclaration)* EOF
            ;

mainClass   :  'class'  identifier  '{'  'public'  'static'  'void'  'main'  '(' 'String' '[' ']'  identifier ')'  '{'  statement? '}' '}'
            ;

classDeclaration    :   'class'  identifier  ('extends'  identifier)? '{'  (varDeclaration )* (methodDeclaration )* '}'
                    ;

varDeclaration      :    type  identifier  ';' |  type  statement
                    ;

methodDeclaration   :  'public'  type  identifier '('  ( type  identifier (  ','  type  identifier )* )? ')' '{'
        ( varDeclaration  )* ( statement  )* 'return'
        expression  ';' '}'
                    ;

statement   :   '{'  ( statement )*  '}'
                |   'if'  '(' expression ')'  statement  'else'  statement
                |   'while' '(' expression ')'  statement
                |   'System.out.println'  '('  expression  ')'  ';'
                |   identifier  '='  expression  ';'
                |   identifier  '['  expression  ']'   '='  expression  ';'
                ;

expression  :   expression  ('&&' | '<' | '+' | '-' | '*')  expression
                |   expression '['  expression  ']'
                |   expression  '.'  'length'
                |   expression  '.'  identifier '(' ( expression(',' expression)*)?')'
                |   INTEGER_LITERAL
                |   'true'
                |   'false'
                |   identifier
                |   'this'
                |   'new'   'int'   '['  expression  ']'
                |   'new'   identifier   '('')'
                |   '!' expression
                |   '('  expression  ')'
                ;

type        :   'int' '[' ']' | 'boolean' | 'int' | identifier
            ;

identifier  : IDENTIFIER
            ;

INTEGER_LITERAL : ('0'..'9')+
                ;

IDENTIFIER  :   (('a'..'z') | ('A'..'Z')) (('a'..'z')|('A'..'Z')|('0'..'9')|('_'))*
            ;

WHITESPACE  :   (' '	|	'\t'	|	'\n'	|	'\r') -> skip
            ;

COMMENT     : '/*' .*? '*/' -> skip
            ;

LINE_COMMENT: '//' ~[\r\n]* -> skip
            ;
