package org.kinkydesign.decibell.xcases.case0;

import org.kinkydesign.decibell.*;
import org.kinkydesign.decibell.annotations.*;

@TableName("B")
public class B extends Component<B> {

    @PrimaryKey
    private String id;

    public B() {
    }

    public B(String id) {
        this.id = id;
    }


}