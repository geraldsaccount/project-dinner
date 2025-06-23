import { UserDto } from "@/types";
import { Avatar, AvatarFallback, AvatarImage } from "../ui/avatar";

type Props = {
  user: UserDto;
  className?: string;
};

const UserAvatar = ({ user, className }: Props) => {
  return (
    <Avatar className={className}>
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
