package com.sinergia.dcargo.client.local;

import java.io.PrintWriter;


import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;


import com.google.gwt.user.client.Window;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

public class GetSetAttributeGenerator extends Generator {

	private static final String IMPL_TYPE_NAME = Function.class.getSimpleName() + "Impl";
	private static final String IMPL_PACKAGE_NAME = Function.class.getPackage().getName();

	@Override
	public String generate(TreeLogger logger, GeneratorContext context, String requestedClass)
			throws UnableToCompleteException {
		TypeOracle typeOracle = context.getTypeOracle();
		JClassType functionType = typeOracle.findType(requestedClass);
		
		//GWT.log("typeOracle: " + typeOracle);
		//Window.alert("typeOracle: " + typeOracle);
		
		//GWT.log("functionType: " + functionType);
		
		assert Function.class.equals(functionType.getClass());

		functionType.getClass().getMethods();
		
		ClassSourceFileComposerFactory composerFactory = new ClassSourceFileComposerFactory(IMPL_PACKAGE_NAME,
				IMPL_TYPE_NAME);

		composerFactory.addImport(Function.class.getCanonicalName());
		composerFactory.addImplementedInterface(Function.class.getName());
		
	

		PrintWriter printWriter = context.tryCreate(logger, IMPL_PACKAGE_NAME, IMPL_TYPE_NAME);
		SourceWriter sourceWriter = composerFactory.createSourceWriter(context, printWriter);

		sourceWriter.print("public Object executeGet(Object obj, String method) {");
//		sourceWriter.print("    Object value = obj.method();");
		sourceWriter.print("    return null;");
		sourceWriter.print("}");

		sourceWriter.commit(logger);
		return IMPL_PACKAGE_NAME + "." + IMPL_TYPE_NAME;
	}
}
