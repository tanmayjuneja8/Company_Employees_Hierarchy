// Tree node
class Node {
	int data;
	Node parent;
	int level;
	int no_of_children;
	int children[] = new int[5];

	public Node(int id) {
		data = id;
		level = 1;
		no_of_children = 0;
	}
}

class Node1 {
	public int value;
	public int pos_in_array;
	public Node1 left;
	public Node1 right;
	public Node1 parent;

	public int height;

	public Node1(int value) {
		this.value = value;
		this.height = 1;
	}
}

class AvlTree {
	public int height(Node1 N) {

		if (N == null)
			return 0;
		return N.height;
	}

	public Node1 rotateRight(Node1 node) {
		Node1 temp = node.left;

		node.left = temp.right;
		computeHeight(node);

		temp.right = node;
		computeHeight(temp);

		temp.parent = node.parent;
		return temp;
	}

	public Node1 rotateLeft(Node1 node) {
		Node1 temp = node.right;

		node.right = temp.left;
		computeHeight(node);

		temp.left = node;
		computeHeight(temp);

		return temp;
	}

	public void computeHeight(Node1 node) {
		node.height = 1;
		if (node.left != null) {
			node.height += node.left.height;
		}
		if (node.right != null) {
			node.height += node.right.height;
		}
	}

	public int getBalance(Node1 node) {
		int balance = 0;
		if (node.left != null) {
			balance += node.left.height;
		}
		if (node.right != null) {
			balance -= node.right.height;
		}

		return balance;
	}

	public Node1 balance(Node1 node) {
		computeHeight(node);
		int balance = getBalance(node);
		if (balance > 1) {
			if (getBalance(node.left) < 0) {
				node.left = rotateLeft(node.left);
			}
			return rotateRight(node);
		} else if (balance < -1) {
			if (getBalance(node.right) > 0) {
				node.right = rotateRight(node.right);
			}
			return rotateLeft(node);
		}

		return node;
	}

	public Node1 root;

	public static int max(int leftChildHeight, int rightChildHeight) {

		if (leftChildHeight < rightChildHeight) {
			return rightChildHeight;
		}
		return leftChildHeight;
	}

	public Node1 insert(Node1 node1, int value) {
		if (node1 == null) {
			return (new Node1(value));
		}

		if (value < node1.value)
			node1.left = insert(node1.left, value);
		else
			node1.right = insert(node1.right, value);
		node1.height = max(height(node1.left), height(node1.right)) + 1;
		int balance = getBalance(node1);

		// Left Left Case
		if (balance > 1 && value < node1.left.value)
			return rotateRight(node1);

		// Right Right Case
		if (balance < -1 && value > node1.right.value)
			return rotateLeft(node1);

		// Left Right Case
		if (balance > 1 && value > node1.left.value) {
			node1.left = rotateLeft(node1.left);
			return rotateRight(node1);
		}

		// Right Left Case
		if (balance < -1 && value < node1.right.value) {
			node1.right = rotateRight(node1.right);
			return rotateLeft(node1);
		}

		/* return the (unchanged) node1 pointer */
		return node1;
	}

	public Node1 search(int key, Node1 node1) {
		if (node1 == null) {
			return null;
		}

		if (key == node1.value) {
			return node1;
		} else if (key < node1.value) {
			return search(key, node1.left);
		} else {
			return search(key, node1.right);
		}
	}

	public Node1 delete(Node1 node, int key) {
		if (node == null) {
			throw new IllegalArgumentException();
		}

		if (key == node.value) {
			if (node.left == null && node.right == null) {
				return null;
			} else if (node.left == null) {
				return node.right;
			} else if (node.right == null) {
				return node.left;
			} else {
				Node1 left = node.right;
				while (left.left != null) {
					left = left.left;
				}

				left.right = deleteLeft(node.right);
				left.left = node.left;

				return balance(left);
			}
		} else if (key < node.value) {
			node.left = delete(node.left, key);
		} else {
			node.right = delete(node.right, key);
		}

		return balance(node);
	}

	// Deletes the leftmost node
	public Node1 deleteLeft(Node1 node) {
		if (node.left == null) {
			return node.right;
		} else {
			node.left = deleteLeft(node.left);
			return balance(node);
		}
	}
}

public class OrgHierarchy implements OrgHierarchyInterface {
	int a[][] = new int[1][];
	Node b[] = new Node[1];
	int level_employees[] = new int[1];
	int level_count_in_array = 0;
	int idx = 0;
	int no_of_employees = 0;
	// root node
	Node1 root;
	AvlTree avl = new AvlTree();

	public boolean isEmpty() {
		// your implementation
		return no_of_employees <= 0;
	}

	public int size() {
		// your implementation
		return no_of_employees;
	}

	public int level(int id) throws IllegalIDException {
		Node1 n = avl.search(id, root);
		return b[n.pos_in_array].level;
	}

	public void hireOwner(int id) throws NotEmptyException {
		// your implementation
		if (idx > 0) {
			throw new NotEmptyException("Not null");
		}
		root = avl.insert(root, id);
		Node root1 = new Node(id);
		b[idx++] = root1; // employees = 1 after this.
		no_of_employees++;
		a[level_count_in_array] = new int[] { id }; // level 1 array.
		level_employees[level_count_in_array++]++; // level 1 pe ek aadmi aagaya.

	}

	public void hireEmployee(int id, int bossid) throws IllegalIDException {
		// your implementation
		Node1 boss = avl.search(bossid, root);
		Node emp = new Node(id);
		if (boss == null || avl.search(id, root) != null) {
			throw new IllegalIDException("null");
		}
		root = avl.insert(root, id); // insert employee.
		emp.parent = b[boss.pos_in_array];
		emp.level = b[boss.pos_in_array].level + 1;
		Node1 emp2 = avl.search(id, root);
		// update employees array.
		if (no_of_employees >= b.length) {
			Node[] s1 = new Node[2 * b.length + 2];
			for (int i = 0; i < b.length; i++) {
				s1[i] = b[i];
			}
			b = s1;
		}
		emp2.pos_in_array = idx; // update in avl
		if (idx <= b.length) {
			Node s3[] = new Node[2 * b.length + 2];
			for (int i = 0; i < b.length; i++) {
				s3[i] = b[i];
			}
			b = s3;
		}
		b[idx++] = emp;
		// update children of boss.
		if (b[boss.pos_in_array].no_of_children >= b[boss.pos_in_array].children.length - 1) {
			int[] s = new int[2 * b[boss.pos_in_array].children.length + 2];
			for (int i = 0; i < b[boss.pos_in_array].children.length; i++) {
				s[i] = b[boss.pos_in_array].children[i];
			}
			b[boss.pos_in_array].children = s;
			b[boss.pos_in_array].children[b[boss.pos_in_array].no_of_children++] = id;
		} else {
			b[boss.pos_in_array].children[b[boss.pos_in_array].no_of_children++] = id;
		}

		// toString
		// update no of employees at level of new employee.
		if (level_employees.length > emp.level - 1) {
			level_employees[emp.level - 1]++;
		}
		if (level_employees.length <= emp.level) {
			int[] s1 = new int[2 * level_employees.length + 2];
			for (int i = 0; i < level_employees.length; i++) {
				s1[i] = level_employees[i];
			}
			level_employees = s1;
			level_employees[emp.level - 1] = 1;
		}
		no_of_employees++;
		if (level_count_in_array < emp.level) {
			if (a.length < emp.level) {
				int[][] s = new int[2 * a.length + 2][];
				for (int i = 0; i < level_count_in_array; i++) {
					s[i] = a[i];
				}
				a = s;
			}
			a[emp.level - 1] = new int[] { id };
			level_count_in_array = emp.level;
			return;
		}
		if (emp.level <= level_count_in_array) {
			int size_of_level_arr = a[emp.level - 1].length;
			if (size_of_level_arr < level_employees[emp.level - 1]) {
				int[] n = new int[2 * size_of_level_arr + 2];
				for (int i = 0; i < a[emp.level - 1].length; i++) {
					n[i] = a[emp.level - 1][i];
				}
				a[emp.level - 1] = n;
				a[emp.level - 1][level_employees[emp.level - 1] - 1] = id;
			} else {
				a[emp.level - 1][level_employees[emp.level - 1] - 1] = id;
			}
		}
	}

	public void swap1(int[] arr, int i, int j) {
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}

	public void fireEmployee(int id) throws IllegalIDException {
		// your implementation
		Node1 p = avl.search(id, root);
		if (p == null || b[p.pos_in_array].no_of_children != 0 || id == a[0][0]) {
			throw new IllegalIDException(null);
		}
		Node parent = b[p.pos_in_array].parent;

		int n = parent.no_of_children;
		for (int i = 0; i < n; i++) {
			if (parent.children[i] == id) {
				swap1(parent.children, i, parent.no_of_children - 1);
				parent.children[parent.no_of_children - 1] = 0;
				parent.no_of_children--;
				break;
			}
		}
		int lev = b[p.pos_in_array].level;
		for (int i = 0; i < a[lev - 1].length; i++) {
			if (a[lev - 1][i] == id) {
				swap1(a[lev - 1], level_employees[lev - 1] - 1, i);
				a[lev - 1][level_employees[lev - 1] - 1] = 0;
				level_employees[lev - 1] -= 1;
				break;
			}
		}
		no_of_employees--;

		root = avl.delete(root, id);
		b[p.pos_in_array] = null;
	}

	public void fireEmployee(int id, int manageid) throws IllegalIDException {
		// your implementation
		Node1 id1 = avl.search(id, root);
		Node1 transfer = avl.search(manageid, root);
		Node parent = b[transfer.pos_in_array].parent;
		int n = b[transfer.pos_in_array].parent.no_of_children;
		int m = b[transfer.pos_in_array].no_of_children;
		b[transfer.pos_in_array].no_of_children += b[id1.pos_in_array].no_of_children;
		if (b[transfer.pos_in_array].no_of_children >= b[transfer.pos_in_array].children.length) {
			int s3[] = new int[2 * b[transfer.pos_in_array].children.length + 2];
			for (int i = 0; i < b[transfer.pos_in_array].children.length; i++) {
				s3[i] = b[transfer.pos_in_array].children[i];
			}
			b[transfer.pos_in_array].children = s3;
		}
		for (int i = m; i < n; i++) {
			b[transfer.pos_in_array].children[i] = b[id1.pos_in_array].children[i - m]; // baccha transfer kardiya
			Node1 pl = avl.search(b[id1.pos_in_array].children[i - m], root); // baccha agaya
			b[pl.pos_in_array].parent = b[transfer.pos_in_array]; // bacche ka parent change kardo
			b[id1.pos_in_array].no_of_children--;
		}
		int lev = b[transfer.pos_in_array].level;
		// array of array update.
		for (int i = 0; i < a[lev - 1].length; i++) {
			if (a[lev - 1][i] == id) {
				swap1(a[lev - 1], level_employees[lev - 1] - 1, i);
				a[lev - 1][level_employees[lev - 1] - 1] = -100;
				level_employees[lev - 1] -= 1;
				break;
			}
		}

		b[id1.pos_in_array] = null;
		// parent update
		int np = parent.no_of_children;
		for (int i = 0; i < np; i++) {
			if (parent.children[i] == id) {
				swap1(parent.children, i, parent.no_of_children - 1);
				parent.children[parent.no_of_children - 1] = 0;
				parent.no_of_children--;
				break;
			}
		}
		root = avl.delete(root, id);
		no_of_employees--;

	}

	public void swap(Node[] arr, int i, int j) {
		Node temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}

	public int boss(int id) throws IllegalIDException {
		// your implementation
		if (id == a[0][0]) {
			return -1;
		}
		Node1 n = avl.search(id, root);
		return b[n.pos_in_array].parent.data;
	}

	public int lowestCommonBoss(int id1, int id2) throws IllegalIDException {
		// your implementation
		Node1 temp1 = avl.search(id1, root);
		Node1 temp2 = avl.search(id2, root);
		if (a[0][0] == id1 || a[0][0] == id2) {
			return -1;
		}
		if (temp1 == null || temp2 == null) {
			throw new IllegalIDException(null);
		}
		if (b[temp1.pos_in_array].level > b[temp2.pos_in_array].level) {
			while (b[temp1.pos_in_array].level != b[temp2.pos_in_array].level) {
				b[temp1.pos_in_array] = b[temp1.pos_in_array].parent;
			}
		}
		if (b[temp1.pos_in_array].level < b[temp2.pos_in_array].level) {
			while (b[temp2.pos_in_array].level != b[temp1.pos_in_array].level) {
				b[temp2.pos_in_array] = b[temp2.pos_in_array].parent;
			}
		}
		while (b[temp1.pos_in_array].data != b[temp2.pos_in_array].data) {
			b[temp1.pos_in_array] = b[temp1.pos_in_array].parent;
			b[temp2.pos_in_array] = b[temp2.pos_in_array].parent;
		}
		return b[temp1.pos_in_array].data;
	}

	public int partition(int[] arr, int low, int high) {
		// pivot
		int i = low - 1;
		int pivot = arr[high];
		for (int j = low; j <= high - 1; j++) {
			// If current element is smaller
			// than the pivot
			if (arr[j] == 0) {
				continue;
			}
			if (arr[j] < pivot) {
				i++;
				swap1(arr, i, j);
			}
		}
		swap1(arr, i + 1, high);
		return (i + 1);
	}

	public void quickSort(int[] arr, int low, int high) {
		if (low < high) {
			int pi = partition(arr, low, high);
			quickSort(arr, low, pi - 1);
			quickSort(arr, pi + 1, high);
		}
	}

	public String toString(int id) throws IllegalIDException {
		// your implementation
		String s = "";
		Node1 n = avl.search(id, root);
		int lev = b[n.pos_in_array].level - 1;
		s += n.value + " ";
		for (int i = lev + 1; i < level_count_in_array; i++) {
			quickSort(a[i], 0, level_employees[i] - 1);
			for (int j = 0; j < level_employees[i]; j++) {
				if (j != level_employees[i] - 1) {
					s += a[i][j] + " ";
					continue;
				}
				s += a[i][j];
			}
			if (i != level_count_in_array - 1) {
				s += " ";
			}
		}
		return s;
	}

	public static void main(String[] args) throws NotEmptyException, IllegalIDException {
	}

}
