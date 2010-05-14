package org.kinkydesign.decibell.xcases.case0;

import org.kinkydesign.decibell.Component;
import org.kinkydesign.decibell.annotations.Entry;
import org.kinkydesign.decibell.annotations.PrimaryKey;
import org.kinkydesign.decibell.annotations.TableName;


@TableName("REMOTE")
public class Remote extends Component<Remote> {

    @PrimaryKey
    public String id;

    @Entry
    public long myEntry;

}