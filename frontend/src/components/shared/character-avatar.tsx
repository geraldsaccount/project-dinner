import { CharacterDetailDto } from "@/types";
import { Avatar, AvatarFallback, AvatarImage } from "../ui/avatar";
import { cn } from "@/lib/utils";

type Props = {
  character: CharacterDetailDto;
  className?: string;
};

const CharacterAvatar = ({ character, className }: Props) => {
  return (
    <Avatar
      className={cn("border-2 border-muted-foreground shrink-0", className)}
    >
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
