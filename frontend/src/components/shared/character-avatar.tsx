import { CharacterDetailDto } from "@/types";
import { Avatar, AvatarFallback, AvatarImage } from "../ui/avatar";

type Props = {
  character: CharacterDetailDto;
  className?: string;
};

const CharacterAvatar = ({ character, className }: Props) => {
  return (
    <Avatar className={className}>
      <AvatarImage src={`data:image/jpeg;base64,${character.avatarData}`} />
      <AvatarFallback>
        {character.name
          .split(" ")
          .map((n: string) => n[0])
          .join("")}
      </AvatarFallback>
    </Avatar>
  );
};

export default CharacterAvatar;
