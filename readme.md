## To-Do

### Insertion

- [ ] At root
- [ ] At 2 and 3 nodes
- [ ] At 4 nodes
    - [ ] Split 
    - [ ] Push up

## Implementation Notes

- Append to existing node if it has < 3 children
- Only leaf nodes can be directly given new values
- Non-leaf nodes must receive their new values by being pushed to from a child!
- When splitting a 4-node, add 2 leftmost children to left node and 2 rightmost children to right node

### Prototypical Up-Pushing

- If pushing up and is left child, place at left
- If pushing up and is right child, place at right
- If pushing up and is not left or right child, place at middle!
