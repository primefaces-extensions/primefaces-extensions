/*
 * Copyright 2011-2020 PrimeFaces Extensions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.primefaces.extensions.showcase.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.extensions.event.CompleteEvent;

/**
 * CodeMirrorController
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 */
@Named
@ViewScoped
public class CodeMirrorController implements Serializable {

    private static final long serialVersionUID = 20111020L;

    private String content = "function test() { console.log('test'); }";
    private String mode = "javascript";
    private String theme = "blackboard";
    private String keymap = "default";

    public void changeMode() {
        if (mode.equals("css")) {
            mode = "javascript";
        }
        else {
            mode = "css";
        }
    }

    public void submitContent() {
        final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
                    "CodeMirror content: " + content);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public List<String> complete(final CompleteEvent event) {
        final ArrayList<String> suggestions = new ArrayList<String>();

        suggestions.add("context: " + event.getContext());
        suggestions.add("token: " + event.getToken());

        return suggestions;
    }

    public List<String> getThemes() {
        final List<String> results = new ArrayList<String>();

        results.add("3024-day");
        results.add("3024-night");
        results.add("abcdef");
        results.add("ambiance");
        results.add("ambiance-mobile");
        results.add("base16-dark");
        results.add("base16-light");
        results.add("bespin");
        results.add("blackboard");
        results.add("cobalt");
        results.add("colorforth");
        results.add("dracula");
        results.add("eclipse");
        results.add("elegant");
        results.add("erlang-dark");
        results.add("hopscotch");
        results.add("icecoder");
        results.add("isotope");
        results.add("lesser-dark");
        results.add("liquibyte");
        results.add("material");
        results.add("mbo");
        results.add("mdn-like");
        results.add("midnight");
        results.add("monokai");
        results.add("neat");
        results.add("neo");
        results.add("night");
        results.add("panda-syntax");
        results.add("paraiso-dark");
        results.add("paraiso-light");
        results.add("pastel-on-dark");
        results.add("railscasts");
        results.add("rubyblue");
        results.add("seti");
        results.add("solarized");
        results.add("the-matrix");
        results.add("tomorrow-night-bright");
        results.add("tomorrow-night-eighties");
        results.add("ttcn");
        results.add("twilight");
        results.add("vibrant-ink");
        results.add("xq-dark");
        results.add("xq-light");
        results.add("yeti");
        results.add("zenburn");

        Collections.sort(results);
        return results;
    }

    public List<String> getModes() {
        final List<String> results = new ArrayList<String>();

        results.add("apl");
        results.add("asn.1");
        results.add("asterisk");
        results.add("asciiarmor");
        results.add("brainfuck");
        results.add("clike");
        results.add("clojure");
        results.add("cmake");
        results.add("cobol");
        results.add("coffeescript");
        results.add("commonlisp");
        results.add("crystal");
        results.add("css");
        results.add("cypher");
        results.add("d");
        results.add("dart");
        results.add("diff");
        results.add("django");
        results.add("dtd");
        results.add("dylan");
        results.add("ebnf");
        results.add("ecl");
        results.add("eiffel");
        results.add("eml");
        results.add("erlang");
        results.add("fcl");
        results.add("fortran");
        results.add("freemarker");
        results.add("gfm");
        results.add("gas");
        results.add("gherkin");
        results.add("go");
        results.add("groovy");
        results.add("haml");
        results.add("haskell");
        results.add("haskell-literate");
        results.add("haxe");
        results.add("htmlembedded");
        results.add("htmlmixed");
        results.add("http");
        results.add("idl");
        results.add("javascript");
        results.add("jinja2");
        results.add("julia");
        results.add("jsx");
        results.add("lua");
        results.add("markdown");
        results.add("mathematica");
        results.add("mbox");
        results.add("mirc");
        results.add("mllike");
        results.add("modelica");
        results.add("mscgen");
        results.add("mumps");
        results.add("nginx");
        results.add("ntriples");
        results.add("octave");
        results.add("oz");
        results.add("pascal");
        results.add("pegjs");
        results.add("perl");
        results.add("php");
        results.add("pig");
        results.add("powershell");
        results.add("properties");
        results.add("protobuf");
        results.add("python");
        results.add("pug");
        results.add("puppet");
        results.add("q");
        results.add("r");
        results.add("rpm");
        results.add("rst");
        results.add("ruby");
        results.add("sass");
        results.add("scala");
        results.add("scheme");
        results.add("shell");
        results.add("sieve");
        results.add("slim");
        results.add("smalltalk");
        results.add("smarty");
        results.add("solr");
        results.add("soy");
        results.add("sparql");
        results.add("swift");
        results.add("spreadsheet");
        results.add("stylus");
        results.add("sql");
        results.add("stex");
        results.add("tcl");
        results.add("textile");
        results.add("tiddlywiki");
        results.add("tiki");
        results.add("toml");
        results.add("tornado");
        results.add("troff");
        results.add("ttcn");
        results.add("ttcn-cfg");
        results.add("turtle");
        results.add("twig");
        results.add("vb");
        results.add("vbscript");
        results.add("velocity");
        results.add("verilog");
        results.add("vhdl");
        results.add("vue");
        results.add("webidl");
        results.add("xml");
        results.add("xquery");
        results.add("yaml");
        results.add("yaml-frontmatter");
        results.add("z80");

        Collections.sort(results);
        return results;
    }

    public List<String> getKeymaps() {
        final List<String> results = new ArrayList<String>();

        results.add("default");
        results.add("emacs");
        results.add("sublime");
        results.add("vim");

        Collections.sort(results);
        return results;
    }

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(final String mode) {
        this.mode = mode;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(final String theme) {
        this.theme = theme;
    }

    public String getKeymap() {
        return keymap;
    }

    public void setKeymap(final String keymap) {
        this.keymap = keymap;
    }
}
