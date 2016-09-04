package org.sapia.tad.cli;

import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.sapia.tad.Datatype;
import org.sapia.tad.help.Help;
import org.sapia.tad.io.Console;
import org.sapia.tad.io.ConsoleOutput;
import org.sapia.tad.io.csv.Csv;
import org.sapia.tad.io.text.Texts;
import org.sapia.tad.math.Sum;
import org.sapia.tad.stat.Stats;
import org.sapia.tad.transform.filter.Filters;
import org.sapia.tad.transform.formula.Formulas;
import org.sapia.tad.transform.index.Indices;
import org.sapia.tad.transform.join.Joins;
import org.sapia.tad.transform.merge.Merges;
import org.sapia.tad.transform.pivot.Pivots;
import org.sapia.tad.transform.range.Ranges;
import org.sapia.tad.transform.slice.Slices;
import org.sapia.tad.transform.sort.Sorts;
import org.sapia.tad.transform.view.Views;
import org.sapia.tad.util.ChainOR;
import org.sapia.tad.util.Data;
import org.sapia.tad.util.Settings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Cli {

  private GroovyShell         shell;
  private StringBuilder       buffer   = new StringBuilder();
  private ChainOR<CmdContext> commands = CommandChainFactory.getDefaultCommands();
  private CliSessionImpl      session;
  
  private Cli() {
    CompilerConfiguration config = new CompilerConfiguration();
    ImportCustomizer imports = new ImportCustomizer();
    imports.addImports(
        
        // Misc
        Datatype.class.getName(),
        Data.class.getName(),
        Settings.class.getName(),
        Help.class.getName(),
        
        // IO
        Csv.class.getName(),
        Texts.class.getName(),
        
        // Computation
        Stats.class.getName(),
        Sum.class.getName(),
        
        // Transformation
        Filters.class.getName(),
        Formulas.class.getName(),
        Indices.class.getName(),
        Joins.class.getName(),
        Merges.class.getName(),
        Pivots.class.getName(),
        Ranges.class.getName(),
        Slices.class.getName(),
        Sorts.class.getName(),
        Views.class.getName()
    );
    
    imports.addStaticStars(
        Data.class.getName(),
        Datatype.class.getName(),
        Help.class.getName(),
        Settings.class.getName()
    );
    
    config.addCompilationCustomizers(imports);
    shell   = new GroovyShell(config);
    session = new CliSessionImpl(shell, new ConsoleOutput() {
      @Override
      public void println(Object content) {
        Console.println(content);
      }
      
      @Override
      public void print(Object content) {
        Console.print(content);
      }
    });
  }
  
  /**
   * @return the Groovy shell.
   */
  public GroovyShell getShell() {
    return shell;
  }
  
  public static void main(String[] args) throws Exception {
    Cli cli = new Cli();
    cli.run();
  }
  
  private void run() throws IOException {
    Console.println("Datasun command-line interface. Type 'exit' to terminate.");
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    while (true) {
      Console.print(">> ");
      String line = input.readLine();
      try {
        doRun(line);
      } catch (Exception e) {
        this.session.getErrorBuffer().addError(e);
        if (e.getMessage() == null) {
          session.message("Error occurred: " + e.getClass().getSimpleName() + " - type 'err' to see exception stack trace");
        } else {
          session.message(e.getMessage() + " - type 'err' to see exception stack trace");
        }
      }
    }
  }
  
  private void doRun(String line) throws Exception {
    if (line != null) {
      line = line.trim();
      
      CmdContext context = new CmdContext(session, line);
      CliCommand cmd = (CliCommand) commands.select(context);
      if (cmd != null) {
        cmd.run(context);
      } else {
        if (line.endsWith(";")) {
          Object returnValue = shell.evaluate(line);
          buffer.delete(0, buffer.length());
          if (returnValue != null) {
            session.getOutput().println(returnValue);
          }
        } else if (line.length() == 0 && buffer.length() != 0) {
          Object returnValue = shell.evaluate(buffer.toString());
          buffer.delete(0, buffer.length());
          if (returnValue != null) {
            session.getOutput().println(returnValue);
          }
        } else {
          buffer.append(line).append(System.lineSeparator());
        } 
      }
    }
  }
  
}
