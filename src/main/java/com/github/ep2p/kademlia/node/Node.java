package com.github.ep2p.kademlia.node;

import com.github.ep2p.kademlia.connection.ConnectionInfo;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Node<ID extends Number, C extends ConnectionInfo> implements Serializable {
    private static final long serialVersionUID = -262155412931861020L;
    protected ID id;
    protected C connectionInfo;
    protected Date lastSeen;

    public static <ID extends Number, C extends ConnectionInfo> Node<ID, C> copy(Node<ID, C> node){
        return new Node<ID, C>().setNode(node);
    }

    protected Node<ID, C> setNode(Node<ID, C> node) {
        this.setConnectionInfo(node.getConnectionInfo());
        this.setId(node.getId());
        this.setLastSeen(node.getLastSeen());
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node<?, ?> node = (Node<?, ?>) o;
        return getId() == node.getId() &&
                Objects.equals(getConnectionInfo(), node.getConnectionInfo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getConnectionInfo());
    }
}
