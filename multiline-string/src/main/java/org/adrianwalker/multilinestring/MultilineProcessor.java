package org.adrianwalker.multilinestring;

import com.sun.tools.javac.model.JavacElements;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

@SupportedAnnotationTypes({"org.adrianwalker.multilinestring.Multiline"})
public final class MultilineProcessor extends AbstractProcessor {

  private JavacElements elementUtils;
  private TreeMaker maker;

  @Override
  public void init(final ProcessingEnvironment procEnv) {

    super.init(procEnv);

    JavacProcessingEnvironment javacProcessingEnv = (JavacProcessingEnvironment) procEnv;
    this.elementUtils = javacProcessingEnv.getElementUtils();
    this.maker = TreeMaker.instance(javacProcessingEnv.getContext());
  }

  @Override
  public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {

    roundEnv.getElementsAnnotatedWith(Multiline.class).forEach(field -> processField(field));

    return true;
  }

  private void processField(final Element field) {

    String docComment = elementUtils.getDocComment(field);
    if (null != docComment) {
      JCTree.JCVariableDecl fieldNode = (JCTree.JCVariableDecl) elementUtils.getTree(field);
      fieldNode.init = maker.Literal(docComment);
    }
  }
}
