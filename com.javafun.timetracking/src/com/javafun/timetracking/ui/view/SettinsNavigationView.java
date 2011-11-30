package com.javafun.timetracking.ui.view;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.javafun.timetracking.ui.resources.ResourceMessages;

/**
 * View with a tree viewer. This class is contributed through the plugin.xml.
 */
public class SettinsNavigationView extends ViewPart {
	private enum TREE_MENU {
		NONE(""), //$NON-NLS-1$
		ADMINISTRATION(ResourceMessages.getString("NavigationView.Administration")), //$NON-NLS-1$
		USERS(ResourceMessages.getString("NavigationView.Users")), //$NON-NLS-1$
		ROLES(ResourceMessages.getString("NavigationView.Roles")), //$NON-NLS-1$
		PERMISSIONS(ResourceMessages.getString("NavigationView.Permissions")), //$NON-NLS-1$
		STATUS(ResourceMessages.getString("NavigationView.Status")), //$NON-NLS-1$
		CONNECTIONS(ResourceMessages.getString("NavigationView.Connections")); //$NON-NLS-1$

		private String _value;

		private TREE_MENU(final String value) {
			_value = value;
		}

		public String getValue() {
			return _value;
		}
	}

	public static final String ID = "com.javafun.core.navigationView"; //$NON-NLS-1$
	private TreeViewer viewer;

	class TreeObject {
		private final String _name;
		private TreeParent _parent;
		private TREE_MENU _menu;

		public TreeObject(final TREE_MENU menu) {
			_name = menu.getValue();
			_menu = menu;
		}

		public String getName() {
			return _name;
		}

		public void setParent(final TreeParent parent) {
			_parent = parent;
		}

		public TreeParent getParent() {
			return _parent;
		}

		public String toString() {
			return getName();
		}

		public TREE_MENU getMenu() {
			return _menu;
		}

		public void setData(final TREE_MENU menu) {
			_menu = menu;
		}
	}

	class TreeParent extends TreeObject {
		private final ArrayList<TreeObject> _children;

		public TreeParent(final TREE_MENU name) {
			super(name);
			_children = new ArrayList<TreeObject>();
		}

		public void addChild(final TreeObject child) {
			_children.add(child);
			child.setParent(this);
		}

		public void removeChild(final TreeObject child) {
			_children.remove(child);
			child.setParent(null);
		}

		public TreeObject[] getChildren() {
			return _children.toArray(new TreeObject[_children.size()]);
		}

		public boolean hasChildren() {
			return _children.size() > 0;
		}
	}

	class ViewContentProvider implements IStructuredContentProvider, ITreeContentProvider {

		public void inputChanged(final Viewer v, final Object oldInput, final Object newInput) {
		}

		public void dispose() {
		}

		public Object[] getElements(final Object parent) {
			return getChildren(parent);
		}

		public Object getParent(final Object child) {
			if (child instanceof TreeObject) {
				return ((TreeObject) child).getParent();
			}
			return null;
		}

		public Object[] getChildren(final Object parent) {
			if (parent instanceof TreeParent) {
				return ((TreeParent) parent).getChildren();
			}
			return new Object[0];
		}

		public boolean hasChildren(final Object parent) {
			if (parent instanceof TreeParent)
				return ((TreeParent) parent).hasChildren();
			return false;
		}
	}

	class ViewLabelProvider extends LabelProvider {

		public String getText(final Object obj) {
			return obj.toString();
		}

		public Image getImage(final Object obj) {
			String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
			if (obj instanceof TreeParent)
				imageKey = ISharedImages.IMG_OBJ_FOLDER;
			return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
		}
	}

	/**
	 * We will set up a dummy model to initialize tree heararchy. In real code,
	 * you will connect to a real model and expose its hierarchy.
	 */
	private TreeObject createMenu() {
		TreeParent administration = new TreeParent(TREE_MENU.ADMINISTRATION);
		TreeObject users = new TreeObject(TREE_MENU.USERS);
		TreeObject roles = new TreeObject(TREE_MENU.ROLES);
		TreeObject permissions = new TreeObject(TREE_MENU.PERMISSIONS);
		administration.addChild(users);
		administration.addChild(permissions);
		administration.addChild(roles);
		TreeParent statistics = new TreeParent(TREE_MENU.STATUS);
		TreeObject connections = new TreeObject(TREE_MENU.CONNECTIONS);
		statistics.addChild(connections);

		TreeParent root = new TreeParent(TREE_MENU.NONE);
		root.addChild(administration);
		root.addChild(statistics);
		return root;
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(final Composite parent) {
		viewer = new TreeViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setInput(createMenu());
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(final DoubleClickEvent event) {
				StructuredSelection selection = (StructuredSelection) viewer.getSelection();
				for (Iterator<?> it = selection.iterator(); it.hasNext();) {
					Object obj = it.next();
					if (obj instanceof TreeObject) {
						TREE_MENU menu = ((TreeObject) obj).getMenu();
						switch (menu) {
						case USERS:

							break;
						case ROLES:

							break;
						case PERMISSIONS:

							break;
						case STATUS:

							break;
						case CONNECTIONS:

							break;
						default:
							break;
						}
					}
				}
				selection.isEmpty();
			}
		});
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}