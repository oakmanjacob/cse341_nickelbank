package view;

import dao.Branch;
import util.IOManager;

import java.util.List;

public class Branch_View {

    public static Branch getBranch()
    {
        System.out.println("Which branch are you banking at?");
        List<Branch> branch_list = Branch.getAllBranch();
        Branch branch = null;

        if (branch_list.size() == 0) {
            branch = null;
        }
        else if (branch_list.size() == 1)
        {
            branch = branch_list.get(0);
        }
        else {
            Object object = IOManager.handleTable(branch_list.toArray(), 5, false);
            if (object instanceof Branch) {
                branch = (Branch) object;
            }
        }

        return branch;
    }
}
