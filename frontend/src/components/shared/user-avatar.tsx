import { UserDto } from "@/types";
import { Avatar, AvatarFallback, AvatarImage } from "../ui/avatar";
import { cn } from "@/lib/utils";

type Props = {
  user: UserDto;
  className?: string;
};

const UserAvatar = ({ user, className }: Props) => {
  return (
    <Avatar
      className={cn("border-2 border-muted-foreground shrink-0", className)}
    >
      <AvatarImage src={user.avatarUrl} />
      <AvatarFallback>
        {user.name
          .split(" ")
          .map((n: string) => n[0])
          .join("")}
      </AvatarFallback>
    </Avatar>
  );
};

export default UserAvatar;
