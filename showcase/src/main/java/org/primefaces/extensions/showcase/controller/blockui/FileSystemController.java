/*
 * Copyright (c) 2011-2023 PrimeFaces Extensions
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package org.primefaces.extensions.showcase.controller.blockui;

import java.io.*;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 * TreeBean
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
@Named
@ViewScoped
public class FileSystemController implements Serializable {

    private static final long serialVersionUID = 20111229L;

    private final TreeNode root;
    private TreeNode selectedNode;

    public FileSystemController() {
        root = new DefaultTreeNode("Root", null);

        final TreeNode node0 = new DefaultTreeNode("Folder 0", root);
        final TreeNode node1 = new DefaultTreeNode("Folder 1", root);
        final TreeNode node2 = new DefaultTreeNode("Folder 2", root);
        final TreeNode node00 = new DefaultTreeNode("Folder 0.0", node0);
        final TreeNode node01 = new DefaultTreeNode("Folder 0.1", node0);
        final TreeNode node10 = new DefaultTreeNode("Folder 1.0", node1);
        new DefaultTreeNode("File 1.1", node1);
        new DefaultTreeNode("File 2.0", node2);
        new DefaultTreeNode("File 0.0.0", node00);
        new DefaultTreeNode("File 0.0.1", node00);
        new DefaultTreeNode("File 0.1.0", node01);
        new DefaultTreeNode("File 1.0.0", node10);
    }

    public TreeNode getRoot() {
        return root;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(final TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public void onNodeExpand(final NodeExpandEvent event) {
        doSomething();
    }

    public void onNodeSelect(final NodeSelectEvent event) {
        selectedNode = event.getTreeNode();
        doSomething();
    }

    public void onNodeUnselect(final NodeUnselectEvent event) {
        selectedNode = null;
        doSomething();
    }

    public void doSomething() {
        try {
            // simulate a long running request
            Thread.sleep(1200);
        }
        catch (final Exception e) {
            // Restore interrupted state...
            Thread.currentThread().interrupt();
        }
    }
}
