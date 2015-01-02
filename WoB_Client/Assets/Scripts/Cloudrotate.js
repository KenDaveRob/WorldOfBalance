#pragma strict

var RotationSpeed : int;
function Start () {

}

function Update () {

transform.Rotate(Vector3.up * Time.deltaTime * RotationSpeed);
transform.position.x += .03;
}