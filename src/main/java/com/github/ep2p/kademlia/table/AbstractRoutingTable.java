/* Copyright (c) 2012-2014, 2016. The SimGrid Team.
 * All rights reserved.                                                     */

/* This program is free software; you can redistribute it and/or modify it
 * under the terms of the license (GNU LGPL) which comes with this package. */

package com.github.ep2p.kademlia.table;


import com.github.ep2p.kademlia.Common;
import com.github.ep2p.kademlia.connection.ConnectionInfo;
import com.github.ep2p.kademlia.model.FindNodeAnswer;
import com.github.ep2p.kademlia.node.Node;

import java.util.Collections;
import java.util.Date;
import java.util.Vector;

public abstract class AbstractRoutingTable<ID extends Number, C extends ConnectionInfo, B extends Bucket<ID, C>> implements RoutingTable<ID, C, B> {
  /* LongBucket list */
  protected Vector<B> buckets;
  /* Id of the routing table owner */
  protected ID id;

  public AbstractRoutingTable(ID id) {
    this.id = id;
    buckets = new Vector<B>();
    for (int i = 0; i < Common.IDENTIFIER_SIZE + 1; i++) {
      buckets.add(createBucketOfId(i));
    }
  }

  protected abstract B createBucketOfId(int i);


  /* Updates the routing table with a new value. Returns true if node didnt exist in table before */
  public boolean update(Node<ID, C> node) {
    //Setting last seen date on node
    node.setLastSeen(new Date());
    Bucket<ID, C> bucket = this.findBucket(node.getId());
    if (bucket.contains(node)) {
      //If the element is already in the bucket, we update it.
      bucket.pushToFront(node.getId());
      return false;
    } else {
      bucket.add(node);
      return true;
    }
  }

  public void delete(Node<ID, C> node) {
    Bucket<ID, C> bucket = this.findBucket(node.getId());
    bucket.remove(node);
  }


  /* Returns the closest nodes we know to a given id */
  public FindNodeAnswer<ID, C> findClosest(ID destinationId) {
    FindNodeAnswer<ID, C> findNodeAnswer = new FindNodeAnswer<ID, C>(destinationId);
    Bucket<ID, C> bucket = this.findBucket(destinationId);
    BucketHelper.addToAnswer(bucket, findNodeAnswer, destinationId);

    // For every node (max common.BucketSize and lte identifier size) and add it to answer
    for (int i = 1; findNodeAnswer.size() < Common.BUCKET_SIZE && ((bucket.getId() - i) >= 0 ||
                                    (bucket.getId() + i) <= Common.IDENTIFIER_SIZE); i++) {
      //Check the previous buckets
      if (bucket.getId() - i >= 0) {
        Bucket<ID, C> bucketP = this.buckets.get(bucket.getId() - i);
        BucketHelper.addToAnswer(bucketP, findNodeAnswer, destinationId);
      }
      //Check the next buckets
      if (bucket.getId() + i <= Common.IDENTIFIER_SIZE) {
        Bucket<ID, C> bucketN = this.buckets.get(bucket.getId() + i);
        BucketHelper.addToAnswer(bucketN, findNodeAnswer, destinationId);
      }
    }

    //We sort the list
    Collections.sort(findNodeAnswer.getNodes());
    //We trim the list
    while (findNodeAnswer.size() > Common.FIND_NODE_SIZE) {
      findNodeAnswer.remove(findNodeAnswer.size() - 1); //TODO: Not the best thing.
    }
    return findNodeAnswer;
  }

  public Vector<B> getBuckets() {
    return buckets;
  }

  @Override
  public String toString() {
    StringBuilder string = new StringBuilder("LongRoutingTable [ id=" + id + " ");
    for (Bucket<ID, C> bucket : buckets) {
      if (bucket.size() > 0) {
        string.append(bucket.getId()).append(" ");
      }
    }
    return string.toString();
  }
}
