public class AvatarData {
	public int avatar_id { get; set; }
	public string name { get; set; }
	public short level { get; set; }
	public int coins { get; set; }
	public string last_played { get; set; }
	
	public AvatarData(int avatar_id) {
		this.avatar_id = avatar_id;
	}
}
